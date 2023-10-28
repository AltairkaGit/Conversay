package com.leopold.modules.chat.repos;

import com.leopold.modules.chat.entity.ChatEntity;
import com.leopold.modules.chat.entity.ChatUserRoleEntity;
import com.leopold.modules.chat.entity.key.ChatUserRoleKey;
import com.leopold.modules.user.entity.UserEntity;
import com.leopold.roles.ChatRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ChatUserRoleRepository extends JpaRepository<ChatUserRoleEntity, ChatUserRoleKey> {
    Optional<ChatUserRoleEntity> findByChatAndUserAndRole(ChatEntity chat, UserEntity user, ChatRole role);
    Optional<ChatUserRoleEntity> findTopByChatAndRole(ChatEntity chat, ChatRole role);
    Set<ChatUserRoleEntity> findAllByChatAndRole(ChatEntity chat, ChatRole role);

    @Query("SELECT cur.role FROM ChatUserRoleEntity cur WHERE cur.chat.chatId = :chatId AND cur.user.userId = :userId")
    List<ChatRole> findChatUserRoles(
            @Param("chatId") Long chatId,
            @Param("userId") Long userId
    );

    void deleteAllByUserAndChat(UserEntity user, ChatEntity chat);
    void deleteAllByUserInAndChat(Collection<UserEntity> users, ChatEntity chat);
}
