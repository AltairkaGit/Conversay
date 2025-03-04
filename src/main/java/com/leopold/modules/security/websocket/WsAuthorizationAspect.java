package com.leopold.modules.security.websocket;

import com.leopold.modules.security.jwt.JwtTokenProvider;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import java.util.List;

@Aspect
@Component
public class WsAuthorizationAspect {
    private final JwtTokenProvider jwtTokenProvider;
    @Autowired
    public WsAuthorizationAspect(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Around("@annotation(WsAuthorization)")
    public Object authorizeSubscription(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] methodArgs = joinPoint.getArgs();
        SimpMessageHeaderAccessor accessor = (SimpMessageHeaderAccessor) methodArgs[0];

        try {
            String access = accessor.getNativeHeader("Authorization").get(0);
            if (!jwtTokenProvider.validateAccess(access))
                throw new AuthenticationException("access token is not ok");

            var requestAttributes = accessor.getSessionAttributes();
            if (requestAttributes != null) requestAttributes.put("userId", jwtTokenProvider.getUserId(access));
            accessor.setSessionAttributes(requestAttributes);
        } catch (RuntimeException e) {
            throw new AuthenticationException("access token is not ok");
        }

        return joinPoint.proceed();
    }
}
