package com.leopold.modules.chat.service.impl;

import com.leopold.modules.chat.entity.ChatEntity;
import com.leopold.modules.chat.entity.ChatUserRoleEntity;
import com.leopold.modules.chat.entity.key.ChatUserKey;
import com.leopold.modules.chat.exception.UserAlreadyInTheChatException;
import com.leopold.modules.chat.exception.UserNotInTheChatException;
import com.leopold.lib.validator.Validator;
import com.leopold.lib.validator.impl.MaxLengthValidation;
import com.leopold.lib.validator.impl.MinLengthValidation;
import com.leopold.lib.validator.impl.ValidatorImpl;
import com.leopold.modules.chat.entity.ChatUserEntity;
import com.leopold.modules.chat.repos.ChatUserRoleRepository;
import com.leopold.modules.chat.repos.MessageRepository;
import com.leopold.modules.file.entity.FileEntity;
import com.leopold.modules.security.chatAuthorization.ChatAuthorizationService;
import com.leopold.modules.user.entity.UserEntity;
import com.leopold.modules.chat.repos.ChatRepository;
import com.leopold.modules.chat.repos.ChatUserRepository;
import com.leopold.modules.user.repos.UserRepository;
import com.leopold.modules.chat.service.ChatService;
import com.leopold.roles.ChatRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ChatServiceImpl implements ChatService, ChatAuthorizationService {
    private final ChatRepository chatRepository;
    private final ChatUserRepository chatUserRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ChatUserRoleRepository chatUserRoleRepository;

    @Autowired
    public ChatServiceImpl(
            ChatRepository chatRepository,
            ChatUserRepository chatUserRepository,
            UserRepository userRepository,
            MessageRepository messageRepository,
            ChatUserRoleRepository chatUserRoleRepository
    ) {
        this.chatRepository = chatRepository;
        this.chatUserRepository = chatUserRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.chatUserRoleRepository = chatUserRoleRepository;
    }

    @Override
    public ChatEntity getById(Long chatId) {
        Optional<ChatEntity> chat = chatRepository.findById(chatId);
        if (chat.isEmpty()) throw new NoSuchElementException("chat with this id is not present");
        return chat.get();
    }

    @Override
    @Transactional
    public ChatEntity create(String name, UserEntity creator, Collection<UserEntity> users) {
        ChatEntity chat = new ChatEntity();
        if (name != null) {
            validateName(name);
            chat.setChatName(name);
        } else {
            String chatName = users.stream().limit(3)
                    .map(UserEntity::getUsername)
                    .collect(Collectors.joining(", ", "", "..."));
            chat.setChatName(chatName);
        }
        chatRepository.saveAndFlush(chat);

        addUsers(chat, users);

        ChatUserRoleEntity adminRole = new ChatUserRoleEntity(creator, chat, ChatRole.Admin);
        chatUserRoleRepository.save(adminRole);

        return chat;
    }

    @Override
    public List<ChatRole> getUserChatRoles(ChatEntity chat, UserEntity user) {
        return chatUserRoleRepository.findChatUserRoles(chat.getChatId(), user.getUserId());
    }

    @Override
    public List<ChatRole> getUserChatRoles(Long chatId, Long userId) {
        return chatUserRoleRepository.findChatUserRoles(chatId, userId);
    }

    @Override
    public void addChatUserRole(ChatEntity chat, UserEntity user, ChatRole role) {
        if (!checkUserInChat(chat, user)) throw new IllegalArgumentException("user not in the chat");
        ChatUserRoleEntity adminRole = new ChatUserRoleEntity(user, chat, role);
        chatUserRoleRepository.save(adminRole);
    }

    @Override
    public void delete(ChatEntity chat) {
        chatRepository.delete(chat);
    }

    @Override
    public void deleteById(Long chatId) {
        chatRepository.deleteByChatId(chatId);
    }

    @Override
    public boolean checkUserInChat(ChatEntity chat, UserEntity user) {
        Optional<ChatUserEntity> chatUser = chatUserRepository.findByChatAndUser(chat, user);
        return chatUser.isPresent();
    }

    @Override
    public boolean checkUserInChat(Long chatId, Long userId) {
        Optional<ChatEntity> chat = chatRepository.findById(chatId);
        if (chat.isEmpty()) return false;
        Optional<UserEntity> user = userRepository.findById(userId);
        if (user.isEmpty()) return false;

        return checkUserInChat(chat.get(), user.get());
    }

    @Override
    public void updatePicture(ChatEntity chat, FileEntity picture) {
        chat.setChatPicture(picture);
        chatRepository.saveAndFlush(chat);
    }

    @Override
    public void updateChatName(ChatEntity chat, String chatName) {
        chat.setChatName(chatName);
        chatRepository.saveAndFlush(chat);
    }

    @Override
    public Page<ChatEntity> getUserChats(UserEntity user, Pageable pageable) {
        return chatUserRepository.findUserChats(user, pageable);
    }

    @Override
    public Page<UserEntity> getChatUsers(ChatEntity chat, Pageable pageable) {
        return chatUserRepository.findChatUsers(chat, pageable);
    }

    @Override
    public Stream<UserEntity> getChatUsers(ChatEntity chat) {
        return chatUserRepository.findChatUsers(chat);
    }

    @Override
    public long countUnreadMessages(ChatEntity chat, UserEntity user, Timestamp origin) {
        return messageRepository.countUnreadMessages(chat.getChatId(), user.getUserId(), origin);
    }

    @Override
    public void addUser(ChatEntity chat, UserEntity user) throws Exception {
        if (!checkUserInChat(chat, user))
            throw new UserAlreadyInTheChatException(user.getUsername(), chat.getChatId());
        ChatUserEntity chatUserEntity = new ChatUserEntity(chat, user);
        chatUserRepository.saveAndFlush(chatUserEntity);
    }

    @Override
    public void addUsers(ChatEntity chat, Collection<UserEntity> users) {
        Set<ChatUserEntity> chatUserEntities = users.stream().map(user -> new ChatUserEntity(chat, user)).collect(Collectors.toSet());
        chatUserRepository.saveAllAndFlush(chatUserEntities);
    }

    /** Удаляет участников из чата
     * Если это был direct, то при выходе одного чела чат удаляется автоматически
     * Если групповой, то чат удаляется если в нем не осталось участников
     *
    **/
    @Override
    public void removeUser(ChatEntity chat, UserEntity user) throws UserNotInTheChatException {
        if (!checkUserInChat(chat, user)) throw new UserNotInTheChatException(user.getUsername(), chat.getChatId());

        chatUserRoleRepository.deleteAllByUserAndChat(user, chat);
        chatUserRepository.deleteById(ChatUserKey.valueOf(user.getUserId(), chat.getChatId()));
        if (chat.getChatType().equals(ChatEntity.ChatType.direct)) {
            chatRepository.delete(chat);
        } else if (chat.getChatType().equals(ChatEntity.ChatType.conversation) && chatUserRepository.countByChat(chat) == 0) {
            chatRepository.delete(chat);
        }
    }

    @Override
    public long countChatUsers(ChatEntity chat) {
        return chatUserRepository.countByChat(chat);
    }

    @Override
    public void removeUsers(ChatEntity chat, Collection<UserEntity> users) {
        chatUserRoleRepository.deleteAllByUserInAndChat(users, chat);
        chatUserRepository.deleteAllByUserInAndChat(users, chat);
    }

    private void validateName(String name) {
        Validator<String> nameValidator = new ValidatorImpl<>(
                new MinLengthValidation(3).setNextChain(
                        new MaxLengthValidation(40)
                ));
        nameValidator.validate("ChatName", name);
    }
    @Override
    public Long extractChatIdFromURI(String uri) {
        Matcher matcher = chatIdPattern.matcher(uri);
        if (matcher.find()) {
            String chatIdStr = matcher.group(1);
            return Long.parseLong(chatIdStr);
        }
        return null;
    }

    private static final Pattern chatIdPattern = Pattern.compile("/chat/(\\d+)");
}
