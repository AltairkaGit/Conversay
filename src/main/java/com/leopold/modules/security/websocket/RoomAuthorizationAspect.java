package com.leopold.modules.security.websocket;

import com.leopold.modules.server.entity.ServerChannelEntity;
import com.leopold.modules.server.entity.ServerEntity;
import com.leopold.modules.server.entity.ServerUserEntity;
import com.leopold.modules.server.service.ConversationService;
import com.leopold.modules.server.service.ServerChannelService;
import com.leopold.modules.server.service.ServerService;
import com.leopold.modules.server.service.ServerUserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import java.util.Optional;

@Component
@Aspect
public class RoomAuthorizationAspect {
    private final ServerUserService serverUserService;
    private final ServerChannelService serverChannelService;

    @Autowired
    public RoomAuthorizationAspect(ServerUserService serverUserService, ServerChannelService serverChannelService) {
        this.serverUserService = serverUserService;
        this.serverChannelService = serverChannelService;
    }

    @Around("@annotation(RoomAuthorization)")
    public Object authorizeSubscription(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] methodArgs = joinPoint.getArgs();
        SimpMessageHeaderAccessor accessor = (SimpMessageHeaderAccessor) methodArgs[0];
        Long myId = (Long)accessor.getSessionAttributes().get("userId");
        String conversation = (String)methodArgs[1];

        ServerChannelEntity serverChannelEntity = null;
        try {
            serverChannelEntity = serverChannelService.getChannelById(conversation);
        } catch (Exception ignore) {}

        if (serverChannelEntity == null)  throw new AuthenticationException("No channel with this room id found, " + conversation);

        String serverId = serverChannelEntity.getServer().getServerId();
        Optional<ServerUserEntity> serverUser = serverUserService.getServerUser(serverId, myId);

        if (serverUser.isEmpty()) throw new AuthenticationException("User " + myId + " is not a participant of server: " + serverId);

        return joinPoint.proceed();
    }
}
