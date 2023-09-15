package com.leopold.modules.server.service.impl;

import com.leopold.modules.server.entity.ServerEntity;
import com.leopold.modules.user.entity.UserEntity;
import com.leopold.modules.server.repos.ServerRepository;
import com.leopold.modules.server.repos.ServerUserRepository;
import com.leopold.modules.server.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ServerServiceImpl implements ServerService {
    private final ServerRepository serverRepository;
    private final ServerUserRepository serverUserRepository;

    @Autowired
    public ServerServiceImpl(ServerRepository serverRepository, ServerUserRepository serverUserRepository) {
        this.serverRepository = serverRepository;
        this.serverUserRepository = serverUserRepository;
    }

    @Override
    public Page<ServerEntity> getServers(UserEntity user, Pageable pageable) {
        return serverUserRepository.findUserServers(user, pageable);
    }
}
