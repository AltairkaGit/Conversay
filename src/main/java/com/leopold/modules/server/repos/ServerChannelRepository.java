package com.leopold.modules.server.repos;

import com.leopold.modules.server.entity.ServerChannelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerChannelRepository extends JpaRepository<ServerChannelEntity, Long> {
}
