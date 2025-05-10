package com.leopold.modules.server.repos;

import com.leopold.modules.server.entity.ServerChannelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServerChannelRepository extends JpaRepository<ServerChannelEntity, String> {
    @Query("SELECT s.server.serverId FROM ServerChannelEntity s WHERE s.channelId = :channelId")
    Optional<String> findServerIdByChannelId(@Param("channelId") String channelId);
}
