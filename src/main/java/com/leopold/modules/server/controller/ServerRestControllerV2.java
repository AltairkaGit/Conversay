package com.leopold.modules.server.controller;

import com.leopold.lib.page.PageDto;
import com.leopold.modules.chat.dto.MessageWebsocketDto;
import com.leopold.modules.server.dto.ServerResponseDto;
import com.leopold.modules.server.dto.mapper.ServerResponseMapper;
import com.leopold.modules.server.entity.ServerEntity;
import com.leopold.modules.server.service.ConversationService;
import com.leopold.modules.user.entity.UserEntity;
import com.leopold.modules.server.service.ServerService;
import com.leopold.modules.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/v2/server")
public class ServerRestControllerV2 {
    private final UserService userService;
    private final ServerService serverService;
    private final ServerResponseMapper serverResponseMapper;
    private final ConversationService conversationService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ServerRestControllerV2(
            UserService userService,
            ServerService serverService,
            ServerResponseMapper serverResponseMapper,
            ConversationService conversationService,
            SimpMessagingTemplate messagingTemplate) {
        this.userService = userService;
        this.serverService = serverService;
        this.serverResponseMapper = serverResponseMapper;
        this.conversationService = conversationService;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping(value="")
    @Operation(summary = "get page of your servers")
    public ResponseEntity<PageDto<ServerResponseDto>> getServers(
            @RequestAttribute("reqUserId") Long userId,
            Pageable pageable
    ) {
        UserEntity me = userService.getUserById(userId);
        Page<ServerEntity> servers = serverService.getServers(me, pageable);
        PageDto<ServerResponseDto> res = PageDto.of(serverResponseMapper.convertPage(servers));
        return ResponseEntity.ok(res);
    }

    @GetMapping(value="/{serverId}")
    @Operation(summary = "get a server by id")
    public ResponseEntity<ServerResponseDto> getServer(
            @RequestAttribute("reqUserId") Long userId,
            @PathVariable Long serverId
    ) {
        UserEntity me = userService.getUserById(userId);
        if (!serverService.checkIfServerUser(me, serverId))
            throw new SecurityException("you are not a participant of the server");
        Optional<ServerEntity> server = serverService.getServerById(serverId);
        if (server.isEmpty())
            throw new IllegalArgumentException("there is no server with this id");
        ServerResponseDto res = serverResponseMapper.convert(server.get());
        return ResponseEntity.ok(res);
    }

    @SubscribeMapping("/queue/conversation/{conversationId}")
    public void subscribeChat(
            SimpMessageHeaderAccessor accessor,
            @DestinationVariable("conversationId") String conversation
    ) {
        //Set<String> room = conversationService.getRoomCopy(conversation);
        //conversationService.attachUser(conversation, myId);
    }

    @MessageMapping("/conversation/{conversationId}")
    public void sendMessage(
            SimpMessageHeaderAccessor accessor,
            @DestinationVariable("conversationId") String conversation,
            String sessionDescriptionProtocol
    ) {
        String myId = String.valueOf((Long)accessor.getSessionAttributes().get("userId"));
        messagingTemplate.convertAndSend("/app/queue/conversation/" + conversation, sessionDescriptionProtocol);
    }
}
