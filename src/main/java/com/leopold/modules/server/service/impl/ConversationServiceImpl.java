package com.leopold.modules.server.service.impl;

import com.leopold.modules.server.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class ConversationServiceImpl implements ConversationService {
    private final ReentrantLock mapsReadingLock = new ReentrantLock();
    private final Map<String, Set<String>> conversations = new ConcurrentHashMap<>();
    private final Map<String, String> userConversation = new ConcurrentHashMap<>();
    private final Queue<String> queue = new ConcurrentLinkedQueue<>();
    private final ReentrantLock queueReadingLock = new ReentrantLock();
    private final SimpMessagingTemplate messagingTemplate;
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    @Autowired
    public ConversationServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void attachUser(String conversation, String userId) {
        conversations.compute(conversation, (key, room) -> {
            if (room == null) {
                Set<String> newRoom = new HashSet<>();
                newRoom.add(userId);
                return newRoom;
            } else {
                room.add(userId);
                return room;
            }
        });
        userConversation.put(userId, conversation);
    }

    @Override
    public void detachUserCurrentConversation(String userId, String server) {
        Optional<String> conversation = getCurrentConversation(userId);

        if (conversation.isPresent()) {
            try {
                detachUser(conversation.get(), userId);
                userConversation.remove(userId);
                messagingTemplate.convertAndSend("/app/queue/conversation/" + server + "/room", conversation.get() + ":leave:" + userId);
                System.out.println("user left the room. userId: " + userId + ". current room: " + conversation.get());
            } catch (Exception ignore) {}
        }
    }

    @Override
    public Optional<String> getCurrentConversation(String userId) {
        try {
            return Optional.of(userConversation.get(userId));
        } catch (Exception ignore) {
            return Optional.empty();
        }

    }

    @Override
    public void detachUser(String conversation, String userId) {
        conversations.compute(conversation, (key, room) -> {
            if (room != null) {
                room.remove(userId);
            }
            return room;
        });
    }

    @Override
    public Set<String> getRoomCopy(String conversation) {
        if (conversations.get(conversation) != null) {
            return new HashSet<>(conversations.get(conversation));
        }
        return new HashSet<>();
    }

    @Override
    public void attachUserToQueue(String userId) {
        queue.add(userId);
        System.out.println("User " + userId + " added to queue, size: " + queue.size());
        executorService.submit(() -> tryToMakeConversation(2));
        System.out.println("Attach finished, user  " + userId);
    }

    private void tryToMakeConversation(int usersInConversation) {
        boolean success = false;
        System.out.println("inside executor");
        String[] users = new String[usersInConversation];
        queueReadingLock.lock();
        try {
            if (queue.size() >= usersInConversation) {
                for (int i = 0; i < usersInConversation; i++) {
                    users[i] = queue.poll();
                }
                success = true;
            }
        } finally {
            System.out.println("Успех? " + success);
            queueReadingLock.unlock();
        }
        if (success) {
            String conversation = UUID.randomUUID().toString();

            for (String userId : users) {
                System.out.println("Отправляем " + userId + " conversation " + conversation);
                attachUser(conversation, userId);
                messagingTemplate.convertAndSend("/app/user/" + userId + "/queue/conversation/blind/queue", conversation);
            }
        }
    }
}
