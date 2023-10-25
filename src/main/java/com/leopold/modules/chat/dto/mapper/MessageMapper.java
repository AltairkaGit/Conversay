package com.leopold.modules.chat.dto.mapper;

import com.leopold.modules.chat.dto.MessageResponseDto;
import com.leopold.modules.chat.dto.MessageWebsocketDto;
import com.leopold.modules.file.dto.mapper.FileResponseMapper;
import com.leopold.modules.file.service.FileService;
import com.leopold.modules.user.dto.mapper.UserProfileResponseMapper;
import com.leopold.modules.chat.entity.MessageEntity;
import com.leopold.modules.chat.service.MessageService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = "spring", uses = {UserProfileResponseMapper.class, FileResponseMapper.class})
public abstract class MessageMapper {
    @Autowired
    protected MessageService messageService;
    @Autowired
    protected FileService fileService;

    @Mapping(target = "chatId", source = "chat.chatId")
    @Mapping(target = "replyId", source = "reply.messageId")
    @Mapping(target = "replyContent", source = "reply.content")
    @Mapping(target = "senderId", source = "sender.userId")
    @Mapping(target = "fileUrls", source = "attachedFiles", qualifiedByName = "getFileUrls")
    public abstract MessageResponseDto convert(MessageEntity message);

    public MessageEntity convert(MessageWebsocketDto dto) {
        MessageEntity message = extractBaseMessageEntity(dto.getContent(), dto.getSendTimestamp(), dto.getReplyId());
        message.setAttachedFiles(dto.getFileIds().stream().map(fileId -> fileService.getFile(fileId)).collect(Collectors.toList()));
        return message;
    }

    public MessageEntity convert(MessageResponseDto dto) {
        MessageEntity message = extractBaseMessageEntity(dto.getContent(), dto.getSendTimestamp(), dto.getReplyId());
        message.setAttachedFiles(dto.getFileUrls().stream().map(fileUrl -> fileService.getFile(fileUrl)).collect(Collectors.toList()));
        return message;
    }

    private MessageEntity extractBaseMessageEntity(String content, Timestamp sendTimestamp, Long replyId) {
        MessageEntity message = new MessageEntity();

        message.setContent(content);
        message.setSendTimestamp(sendTimestamp);
        if (replyId != null) {
            Optional<MessageEntity> reply = messageService.getMessageById(replyId);
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
