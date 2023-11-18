package com.leopold.modules.server.repos;

import com.leopold.modules.server.entity.ServerUserRoleEntity;
import com.leopold.modules.server.entity.key.ServerUserRoleKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerUserRoleRepository extends JpaRepository<ServerUserRoleEntity, ServerUserRoleKey> {
}
