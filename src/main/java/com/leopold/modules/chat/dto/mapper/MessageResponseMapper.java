package com.leopold.modules.chat.dto.mapper;

import com.leopold.modules.chat.dto.MessageResponseDto;
import com.leopold.modules.user.dto.mapper.UserProfileResponseMapper;
import com.leopold.modules.chat.entity.MessageEntity;
import com.leopold.modules.chat.service.MessageService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Mapper(componentModel = "spring", uses = {UserProfileResponseMapper.class})
public abstract class MessageResponseMapper {
    @Autowired
    protected MessageService messageService;

    @Mapping(target = "chatId", source = "chat.chatId")
    @Mapping(target = "replyId", source = "reply.messageId")
    @Mapping(target = "replyContent", source = "reply.content")
    @Mapping(target = "senderId", source = "sender.userId")
    public abstract MessageResponseDto convert(MessageEntity message);

    public MessageEntity convert(MessageResponseDto dto) {
        MessageEntity message = new MessageEntity();

        message.setContent(dto.getContent());
        message.setSendTimestamp(dto.getSendTimestamp());
        if (dto.getReplyId() != null) {
            Optional<MessageEntity> reply = messageService.getMessageById(dto.getReplyId());
            reply.ifPresent(message::setReply);
         }

        return message;
    }

    public abstract List<MessageResponseDto> convertList(List<MessageEntity> messageEntities);
    public Page<MessageResponseDto> convertPage(Page<MessageEntity> page) {
        List<MessageResponseDto> list = convertList(page.getContent());
        return new PageImpl<>(list, page.getPageable(), page.getTotalElements());
    }
}
