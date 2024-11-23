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
    private final Map<String, Set<String>> conversations = new ConcurrentHashMap<>();
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
    }

    @Override
    public Set<String> getRoomCopy(String conversation) {
        return new HashSet<>(conversations.get(conversation));
    }

    @Override
    public void attachUserToQueue(String userId) {
        queue.add(userId);
        executorService.submit(() -> tryToMakeConversation(2));
    }

    private void tryToMakeConversation(int usersInConversation) {
        queueReadingLock.lock();
        boolean success = false;
        String[] users = new String[usersInConversation];
        try {
            if (queue.size() >= usersInConversation) {
                for (int i = 0; i < usersInConversation; i++) {
                    users[i] = queue.poll();
                }
                success = true;
            }
        } finally {
            queueReadingLock.unlock();
        }
        if (success) {
            String conversation = UUID.randomUUID().toString();

            for (String userId : users) {
                attachUser(conversation, userId);
                messagingTemplate.convertAndSendToUser(userId, "/conversation/blind/queue", conversation);
            }
        }
    }
}
