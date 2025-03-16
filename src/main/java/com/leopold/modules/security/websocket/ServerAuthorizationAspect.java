package com.leopold.modules.security.websocket;

import com.leopold.modules.server.entity.ServerEntity;
import com.leopold.modules.server.entity.ServerUserEntity;
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
public class ServerAuthorizationAspect {
    private final ServerUserService serverUserService;
    private final ServerService serverService;

    @Autowired
    public ServerAuthorizationAspect(ServerUserService serverUserService, ServerService serverService) {
        this.serverUserService = serverUserService;
        this.serverService = serverService;
    }

    @Around("@annotation(ServerAuthorization)")
    public Object authorizeSubscription(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] methodArgs = joinPoint.getArgs();
        SimpMessageHeaderAccessor accessor = (SimpMessageHeaderAccessor) methodArgs[0];
        Long myId = (Long)accessor.getSessionAttributes().get("userId");
        String serverId = (String)methodArgs[1];

        ServerEntity server = serverService.getServerById(serverId).get();
        Optional<ServerUserEntity> serverUser = serverUserService.getServerUser(server.getServerId(), myId);

        if (serverUser.isEmpty()) throw new AuthenticationException("User " + myId + " is not a participant of server: " + serverId);

        return joinPoint.proceed();
    }
}
