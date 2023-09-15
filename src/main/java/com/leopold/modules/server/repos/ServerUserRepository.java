package com.leopold.modules.server.repos;

import com.leopold.modules.server.entity.ServerEntity;
import com.leopold.modules.server.entity.ServerUserEntity;
import com.leopold.modules.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerUserRepository extends JpaRepository<ServerUserEntity, Long> {
    Page<ServerUserEntity> findAllByUser(UserEntity user, Pageable pageable);
    Page<ServerUserEntity> findAllByServer(ServerEntity server, Pageable pageable);

    default Page<UserEntity> findServerUsers(ServerEntity server, Pageable pageable) {
        return findAllByServer(server, pageable).map(ServerUserEntity::getUser);
    }

    default Page<ServerEntity> findUserServers(UserEntity user, Pageable pageable) {
        return findAllByUser(user, pageable).map(ServerUserEntity::getServer);
    }
}
