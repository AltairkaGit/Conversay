package com.leopold.modules.server.exception;

public class UserAlreadyOnServerException extends RuntimeException {
    public UserAlreadyOnServerException(String serverId, long userId) {
        super("User " + userId + " already on server " + serverId);
    }
}
