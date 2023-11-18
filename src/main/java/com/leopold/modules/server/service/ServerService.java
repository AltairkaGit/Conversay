package com.leopold.modules.server.service;

import com.leopold.modules.server.entity.ServerEntity;
import com.leopold.modules.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface ServerService {
    Page<ServerEntity> getServers(UserEntity user, Pageable pageable);
    boolean checkIfServerUser(UserEntity user, Long serverId);
    Optional<ServerEntity> getServerById(Long serverId);
}
