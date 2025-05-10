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
            System.out.println(accessor.getMessageHeaders());
            String access = accessor.getNativeHeader("Authorization").get(0);
            System.out.println("access: " + access);
            if (!jwtTokenProvider.validateAccess(access))
                throw new AuthenticationException("access token is not valid");
            System.out.println("пися попа 1");
            var requestAttributes = accessor.getSessionAttributes();
            System.out.println("пися попа 2, " + requestAttributes);
            if (requestAttributes != null) requestAttributes.put("userId", jwtTokenProvider.getUserId(access));
            System.out.println("пися попа 3, " + requestAttributes);
            accessor.setSessionAttributes(requestAttributes);
            System.out.println("пися попа 4, " + accessor.getSessionAttributes());
        } catch (RuntimeException e) {
            throw new AuthenticationException("access token retrieving failure");
        }

        return joinPoint.proceed();
    }
}
