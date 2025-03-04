package com.leopold.modules.server.controller;

import com.leopold.lib.page.PageDto;
import com.leopold.modules.file.entity.FileEntity;
import com.leopold.modules.file.service.FileService;
import com.leopold.modules.security.websocket.WsAuthorization;
import com.leopold.modules.server.dto.*;
import com.leopold.modules.server.dto.mapper.ServerChannelMapper;
import com.leopold.modules.server.dto.mapper.ServerMapper;
import com.leopold.modules.server.dto.mapper.ServerUserMapper;
import com.leopold.modules.server.entity.ServerChannelEntity;
import com.leopold.modules.server.entity.ServerEntity;
import com.leopold.modules.server.entity.ServerUserEntity;
import com.leopold.modules.server.service.ConversationService;
import com.leopold.modules.server.service.ServerChannelService;
import com.leopold.modules.server.service.ServerUserService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/api/v2/server")
public class ServerRestControllerV2 {
    private final UserService userService;
    private final ServerService serverService;
    private final ServerUserService serverUserService;
    private final ServerMapper serverMapper;
    private final ConversationService conversationService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ServerChannelMapper serverChannelMapper;
    private final ServerChannelService serverChannelService;
    private final ServerUserMapper serverUserMapper;
    private final FileService fileService;
    @Autowired
    public ServerRestControllerV2(
            UserService userService,
            ServerService serverService,
            ServerUserService serverUserService,
            ServerMapper serverMapper,
            ConversationService conversationService,
            SimpMessagingTemplate messagingTemplate,
            ServerChannelMapper serverChannelMapper,
            ServerChannelService serverChannelService,
            ServerUserMapper serverUserMapper, FileService fileService) {
        this.userService = userService;
        this.serverService = serverService;
        this.serverUserService = serverUserService;
        this.serverMapper = serverMapper;
        this.conversationService = conversationService;
        this.messagingTemplate = messagingTemplate;
        this.serverChannelMapper = serverChannelMapper;
        this.serverChannelService = serverChannelService;
        this.serverUserMapper = serverUserMapper;
        this.fileService = fileService;
    }

    @GetMapping(value="")
    @Operation(summary = "get page of your servers")
    public ResponseEntity<PageDto<ServerDto>> getServers(
            @RequestAttribute("reqUserId") Long userId,
            Pageable pageable
    ) {
        UserEntity me = userService.getUserById(userId);
        Page<ServerEntity> servers = serverUserService.getUserServers(me, pageable);
        PageDto<ServerDto> res = PageDto.of(serverMapper.convertPage(servers));
        return ResponseEntity.ok(res);
    }

    @GetMapping(value="/{serverId}/users")
    @Operation(summary = "get page of your server users")
    public ResponseEntity<PageDto<ServerUserProfileDto>> getServerUsers(
            @RequestAttribute("reqUserId") Long userId,
            @PathVariable String serverId,
            Pageable pageable
    ) {
        UserEntity me = userService.getUserById(userId);
        ServerEntity server = serverService.getServerById(serverId).get();
        Page<ServerUserEntity> servers = serverUserService.getServerUsers(server, pageable);
        PageDto<ServerUserProfileDto> res = PageDto.of(serverUserMapper.convertPage(servers));
        return ResponseEntity.ok(res);
    }

    @PostMapping(value="")
    @Operation(summary = "create a server")
    public ResponseEntity<ServerDto> createServer(
            @RequestAttribute("reqUserId") Long userId,
            CreateServerDto dto
    ) {
        UserEntity me = userService.getUserById(userId);
        ServerEntity server = serverService.createServer(me, dto.getServername());
        ServerDto res = serverMapper.convert(server);
        return ResponseEntity.ok(res);
    }

    @PostMapping(value="/{serverId}/user")
    @Operation(summary = "add a user to server")
    public ResponseEntity<Void> addUsers(
            @RequestAttribute("reqUserId") Long userId,
            @PathVariable String serverId,
            AddServerUserDto dto
    ) {
        UserEntity me = userService.getUserById(userId);
        Optional<ServerEntity> server = serverService.getServerById(serverId);
        if (server.isEmpty()) throw new IllegalArgumentException("there is no server with id:" + serverId);
        if (!serverService.checkIfServerUser(me, serverId)) throw new SecurityException("user are not a server participant");
        UserEntity participant = userService.getUserById(dto.getUserid());
        serverUserService.addUser(serverId, participant.getUserId());
        return ResponseEntity.ok().build();
    }

    @GetMapping(value="/{serverId}")
    @Operation(summary = "get a server by id")
    public ResponseEntity<ServerExtendedDto> getServer(
            @RequestAttribute("reqUserId") Long userId,
            @PathVariable String serverId
    ) {
        UserEntity me = userService.getUserById(userId);
        if (!serverService.checkIfServerUser(me, serverId))
            throw new SecurityException("you are not a participant of the server");
        Optional<ServerEntity> server = serverService.getServerById(serverId);
        if (server.isEmpty())
            throw new IllegalArgumentException("there is no server with this id");
        ServerExtendedDto res = serverMapper.convertExtended(server.get());
        return ResponseEntity.ok(res);
    }

    @PostMapping(value="/{serverId}/channel")
    @Operation(summary = "create a channel")
    public ResponseEntity<ServerChannelDto> createChannel(
            @RequestAttribute("reqUserId") Long userId,
            @PathVariable String serverId,
            @RequestBody CreateChannelDto dto
            ) {
        UserEntity me = userService.getUserById(userId);
        if (!serverService.checkIfServerUser(me, serverId))
            throw new SecurityException("you are not a participant of the server");
        Optional<ServerEntity> server = serverService.getServerById(serverId);
        if (server.isEmpty())
            throw new IllegalArgumentException("there is no server with this id");
        ServerChannelEntity channel = serverChannelService.createChannel(server.get(), dto.getChannelName(), dto.getChannelType());
        ServerChannelDto res = serverChannelMapper.convert(channel);
        return ResponseEntity.ok(res);
    }

    @PutMapping(value= "/{serverId}/picture")
    @Operation(summary = "upload a multipart file, 200 and FileResponseDto if ok, 400, 500 otherwise")
    public ResponseEntity<ServerExtendedDto> updateServerPicture(
            @RequestAttribute("reqUserId") Long userId,
            @PathVariable String serverId,
            @RequestPart("file") MultipartFile file
    ) {
        UserEntity me = userService.getUserById(userId);
        if (!serverService.checkIfServerUser(me, serverId))
            throw new SecurityException("you are not a participant of the server");
        Optional<ServerEntity> server = serverService.getServerById(serverId);
        if (server.isEmpty())
            throw new IllegalArgumentException("there is no server with this id");

        FileEntity fileEntity = fileService.uploadFile(file);
        serverService.updatePicture(server.get(), fileEntity);

        ServerExtendedDto res = serverMapper.convertExtended(serverService.getServerById(serverId).get());
        return ResponseEntity.ok(res);
    }

    @WsAuthorization
    @SubscribeMapping("/queue/conversation/{conversationId}")
    public void subscribeTalk(
            SimpMessageHeaderAccessor accessor,
            @DestinationVariable("conversationId") String conversation
    ) {
        String myId = String.valueOf((Long)accessor.getSessionAttributes().get("userId"));
        System.out.println("User: " + myId + " subscribed conversation " + conversation);
        System.out.println("===========================================================");
    }

    @WsAuthorization
    @SubscribeMapping("/queue/testSubscribe")
    public void testSubscribe(
            SimpMessageHeaderAccessor accessor
    ) {
        String myId = String.valueOf((Long)accessor.getSessionAttributes().get("userId"));
        System.out.println("User: " + myId + " ебал твою мать");
        System.out.println("===========================================================");
    }

    @WsAuthorization
    @MessageMapping("/testSubscribe")
    public void testSend(
            SimpMessageHeaderAccessor accessor,
            String message
    ) {
        String myId = String.valueOf((Long)accessor.getSessionAttributes().get("userId"));
        System.out.println("User: " + myId + " прислал эту хуйню: " + message);

        messagingTemplate.convertAndSend("/app/queue/testSubscribe", message);
    }

    @WsAuthorization
    @MessageMapping("/conversation/{conversationId}")
    public void sendSDP(
            SimpMessageHeaderAccessor accessor,
            @DestinationVariable("conversationId") String conversation,
            String sessionDescriptionProtocol
    ) {
        String myId = String.valueOf((Long)accessor.getSessionAttributes().get("userId"));
        System.out.println("User: " + myId + " sends in conversation " + conversation + " message: " + sessionDescriptionProtocol);

        messagingTemplate.convertAndSend("/app/queue/conversation/" + conversation, sessionDescriptionProtocol);
    }

    @WsAuthorization
    @SubscribeMapping("/user/{userId}/queue/conversation/blind/queue")
    public void getInBlindTalkQueue(
            SimpMessageHeaderAccessor accessor,
            @DestinationVariable("userId") String userId
    ) {
        String myId = String.valueOf((Long)accessor.getSessionAttributes().get("userId"));
        System.out.println("User: " + myId + " attempted to attach queue, userId: " + userId);
        System.out.println(accessor);
        if (myId.equals(userId)) {
            conversationService.attachUserToQueue(myId);
        }
    }

}
