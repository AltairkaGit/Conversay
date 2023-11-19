package com.leopold.modules.server.service;

import com.leopold.modules.file.entity.FileEntity;
import com.leopold.modules.server.entity.ServerEntity;
import com.leopold.modules.user.entity.UserEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface ServerService {
    ServerEntity createServer(@NotNull UserEntity creator, @NotNull String serverName);
    void updatePicture(ServerEntity server, FileEntity picture);
    Page<ServerEntity> getServers(UserEntity user, Pageable pageable);
    Page<UserEntity> getUsers(ServerEntity server, Pageable pageable);
    boolean checkIfServerUser(UserEntity user, Long serverId);
    Optional<ServerEntity> getServerById(Long serverId);
}
