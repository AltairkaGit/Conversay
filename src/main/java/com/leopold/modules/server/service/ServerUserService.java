package com.leopold.modules.server.service;

import com.leopold.modules.server.entity.ServerEntity;
import com.leopold.modules.server.entity.ServerUserEntity;
import com.leopold.modules.server.exception.UserAlreadyOnServerException;
import com.leopold.modules.server.exception.UserNotOnServerException;
import com.leopold.modules.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.Optional;

public interface ServerUserService {
    Optional<ServerUserEntity> getServerUser(long serverId, long userId);
    void addUser(long serverId, long userId) throws UserAlreadyOnServerException;
    void kickUser(long serverId, long userId) throws UserNotOnServerException;
    void kickUsers(long serverId, Collection<Long> userIds);
    Page<ServerUserEntity> getServerUsers(ServerEntity server, Pageable pageable);
    Page<ServerEntity> getUserServers(UserEntity user, Pageable pageable);
}
