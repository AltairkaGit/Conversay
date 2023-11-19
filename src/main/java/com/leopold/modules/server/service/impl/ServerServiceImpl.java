package com.leopold.modules.server.service.impl;

import com.leopold.modules.file.entity.FileEntity;
import com.leopold.modules.server.entity.ServerEntity;
import com.leopold.modules.server.entity.ServerUserEntity;
import com.leopold.modules.server.service.ServerUserService;
import com.leopold.modules.user.entity.UserEntity;
import com.leopold.modules.server.repos.ServerRepository;
import com.leopold.modules.server.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServerServiceImpl implements ServerService {
    private final ServerRepository serverRepository;
    private final ServerUserService serverUserService;

    @Autowired
    public ServerServiceImpl(ServerRepository serverRepository, ServerUserService serverUserService) {
        this.serverRepository = serverRepository;
        this.serverUserService = serverUserService;
    }

    @Override
    public ServerEntity createServer(UserEntity creator, String serverName) {
        ServerEntity server = new ServerEntity();
        server.setServername(serverName);
        serverRepository.saveAndFlush(server);
        serverUserService.addUser(server.getServerId(), creator.getUserId());
        //add admin role

        return server;
    }

    @Override
    public void updatePicture(ServerEntity server, FileEntity picture) {
        server.setServerPicture(picture);
        serverRepository.saveAndFlush(server);
    }

    @Override
    public Page<ServerEntity> getServers(UserEntity user, Pageable pageable) {
        return serverUserService.getUserServers(user, pageable);
    }

    @Override
    public Page<UserEntity> getUsers(ServerEntity server, Pageable pageable) {
        return serverUserService.getServerUsers(server, pageable);
    }

    @Override
    public boolean checkIfServerUser(UserEntity user, Long serverId) {
        Optional<ServerUserEntity> serverUser = serverUserService.getServerUser(serverId, user.getUserId());
        return serverUser.isPresent();
    }

    @Override
    public Optional<ServerEntity> getServerById(Long serverId) {
        return serverRepository.findById(serverId);
    }
}
