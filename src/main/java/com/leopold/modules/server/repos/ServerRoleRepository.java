package com.leopold.modules.server.repos;

import com.leopold.modules.server.entity.ServerRoleEntity;
import com.leopold.modules.server.entity.key.ServerRoleKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerRoleRepository extends JpaRepository<ServerRoleEntity, ServerRoleKey> {
}
