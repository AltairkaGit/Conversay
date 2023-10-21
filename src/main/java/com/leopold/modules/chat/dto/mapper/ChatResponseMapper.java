package com.leopold.modules.chat.dto.mapper;

import com.leopold.modules.file.dto.mapper.FileResponseMapper;
import com.leopold.modules.file.service.FileService;
import com.leopold.modules.user.dto.mapper.UserProfileResponseMapper;
import com.leopold.modules.chat.dto.ChatResponseDto;
import com.leopold.modules.chat.entity.ChatEntity;
import com.leopold.modules.file.entity.FileEntity;
import com.leopold.modules.user.entity.UserEntity;
import com.leopold.modules.chat.service.ChatService;
import com.leopold.modules.chat.service.MessageService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = "spring")
public abstract class ChatResponseMapper {
    @Autowired
    protected ChatService chatService;
    @Autowired
    protected MessageService messageService;

    @Autowired
    protected MessageMapper messageResponseMapper;

    @Autowired
    protected UserProfileResponseMapper userProfileResponseMapper;
    @Autowired
    protected FileResponseMapper fileResponseMapper;
    @Autowired
    protected FileService fileService;

    public ChatResponseDto convert(ChatEntity chat, UserEntity me) {
        ChatResponseDto chatResponseDto = new ChatResponseDto();
        chatResponseDto.setChatId(chat.getChatId());
        chatResponseDto.setChatType(chat.getChatType());
        if (chatResponseDto.getChatType() == ChatEntity.ChatType.direct) {
            Optional<UserEntity> userEntity = chatService.getChatUsers(chat)
                    .filter(user -> !user.getUserId().equals(me.getUserId()))
                    .findAny();
            if (userEntity.isPresent()) {
                UserEntity companion = userEntity.get();
                chatResponseDto.setChatName(companion.getUsername());
                Optional<FileEntity> picture = companion.getProfilePicture();
                chatResponseDto.setChatPictureUrl(fileResponseMapper.map(picture));
            }
        }
        else if (chatResponseDto.getChatType() == ChatEntity.ChatType.conversation) {
            chatResponseDto.setChatName(chat.getChatName());
            Optional<FileEntity> picture = chat.getChatPicture();
            chatResponseDto.setChatPictureUrl(fileResponseMapper.map(picture));
        }
        if (chatResponseDto.getChatPictureUrl() == null) {
            chatResponseDto.setChatPictureUrl(fileService.composeUrl("defaultServer.png"));
        }
        if (chatResponseDto.getChatName() == null) {
            int count = 3;
            String chatName = chatService.getChatUsers(chat)
                            .map(UserEntity::getUsername)
                            .limit(count)
                            .collect(Collectors.joining(", "));
            if (chatService.getChatUsers(chat).count() > count)
                chatName += "...";
            chatResponseDto.setChatName(chatName);
        }

        return chatResponseDto;
    }

    public List<ChatResponseDto> convertList(List<ChatEntity> chats, UserEntity me) {
        return chats.stream()
                .map(chat -> convert(chat, me))
                .collect(Collectors.toList());
    }
    public Page<ChatResponseDto> convertPage(Page<ChatEntity> page, UserEntity me) {
        List<ChatResponseDto> list = convertList(page.getContent(), me);
        return new PageImpl<>(list, page.getPageable(), page.getTotalElements());
    }
}
