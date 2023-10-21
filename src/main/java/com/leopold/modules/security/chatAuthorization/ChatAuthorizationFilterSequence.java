package com.leopold.modules.security.chatAuthorization;

import com.leopold.modules.chat.service.ChatService;
import com.leopold.modules.security.composer.FilterSequence;
import com.leopold.modules.security.composer.context.ComposerContext;
import com.leopold.modules.security.composer.context.ComposerContextEnum;
import com.leopold.roles.ChatRoles;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Order(2)
public class ChatAuthorizationFilterSequence implements FilterSequence {
    private final ChatAuthorizationService chatAuthorizationService;
    public ChatAuthorizationFilterSequence(ChatAuthorizationService chatAuthorizationService) {
        this.chatAuthorizationService = chatAuthorizationService;
    }

    /** Фильтр рассчитам на то что первичная авторизация пользователя прошла
     *  Тащим UserId из атрибутов запроса
     *  Тащим ChatId из URI
     *  Проверяем что пользователь в чате
     *  Закидываем в контекст новую Authentication
     *  с дополнительными правами
    **/
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, ComposerContext context)
            throws IOException, ServletException, AuthenticationException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        Long userId = context.get(ComposerContextEnum.UserId);
        Long chatId = chatAuthorizationService.extractChatIdFromURI(httpServletRequest.getRequestURI());
        List<GrantedAuthority> chatAuthorities = new ArrayList<>();
        if (userId != null && chatId != null) {
            if (chatAuthorizationService.checkUserInChat(chatId, userId)) {
                SimpleGrantedAuthority participant = new SimpleGrantedAuthority(ChatRoles.Participant.toString());
                chatAuthorities.add(participant);
            }
        }
        context.put(ComposerContextEnum.ChatAuthorities, chatAuthorities);
    }
}
