package com.leopold.modules.chat.repos;

import com.leopold.modules.chat.entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, Long> {
    void deleteByChatId(Long chatId);
}
