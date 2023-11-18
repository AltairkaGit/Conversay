package com.leopold.modules.server.repos;

import com.leopold.modules.server.entity.ServerRoleActionEntity;
import com.leopold.modules.server.entity.key.ServerRoleActionKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerRoleActionRepository extends JpaRepository<ServerRoleActionEntity, ServerRoleActionKey> {

}
