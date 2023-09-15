package com.leopold.modules.server.service;

import com.leopold.modules.server.entity.ServerEntity;
import com.leopold.modules.user.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Transactional
public interface ServerService {
    Page<ServerEntity> getServers(UserEntity user, Pageable pageable);
}
