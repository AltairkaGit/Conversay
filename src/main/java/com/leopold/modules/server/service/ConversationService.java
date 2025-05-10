package com.leopold.modules.server.service;

import java.util.Optional;
import java.util.Set;

public interface ConversationService {
    void attachUserToQueue(String userId);
    void attachUser(String conversation, String userId);
    void detachUserCurrentConversation(String userId, String server);
    Optional<String> getCurrentConversation(String userId);
    void detachUser(String conversation, String userId);
    Set<String> getRoomCopy(String conversation);
}
