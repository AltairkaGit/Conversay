package com.leopold.modules.server.controller;

import com.leopold.modules.server.dto.ServerResponseDto;
import com.leopold.modules.server.dto.mapper.ServerResponseMapper;
import com.leopold.modules.server.entity.ServerEntity;
import com.leopold.modules.user.entity.UserEntity;
import com.leopold.modules.server.service.ServerService;
import com.leopold.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/servers")
public class ServerRestControllerV1 {
    private final UserService userService;
    private final ServerService serverService;
    private final ServerResponseMapper serverResponseMapper;

    @Autowired
    public ServerRestControllerV1(UserService userService, ServerService serverService, ServerResponseMapper serverResponseMapper) {
        this.userService = userService;
        this.serverService = serverService;
        this.serverResponseMapper = serverResponseMapper;
    }

    @GetMapping(value="")
    public ResponseEntity<Page<ServerResponseDto>> getChats(
            @RequestAttribute("reqUserId") Long userId,
            Pageable pageable
    ) {
        UserEntity me = userService.getUserById(userId);
        Page<ServerEntity> servers = serverService.getServers(me, pageable);
        Page<ServerResponseDto> res = serverResponseMapper.convertPage(servers);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
