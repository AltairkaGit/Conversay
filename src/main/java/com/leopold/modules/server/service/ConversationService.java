package com.leopold.modules.server.service;

import java.util.Set;

public interface ConversationService {
    void attachUserToQueue(String userId);
    void attachUser(String conversation, String userId);
    Set<String> getRoomCopy(String conversation);
}
