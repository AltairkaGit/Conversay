package com.leopold.modules.security.chatAuthorization;

import com.leopold.modules.chat.service.ChatService;
import com.leopold.roles.ChatRoles;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatAuthorizationFilter extends GenericFilterBean {
    private final ChatService chatService;
    public ChatAuthorizationFilter(ChatService chatService) {
        this.chatService = chatService;
    }

    /** Фильтр рассчитам на то что первичная авторизация пользователя прошла
     *  Тащим UserId из атрибутов запроса
     *  Тащим ChatId из URI
     *  Проверяем что пользователь в чате
     *  Закидываем в контекст новую Authentication
     *  с дополнительными правами
    **/
    //TODO: проверка  userId, chatId что сущности существуют
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        Long userId = (Long)request.getAttribute("reqUserId");
        Long chatId = extractChatId(httpServletRequest.getRequestURI());
        if (userId != null && chatId != null) {
            boolean userInChat = chatService.checkUserInChat(chatId, userId);
            if (userInChat) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication != null) {
                    List<GrantedAuthority> authorities = new ArrayList<>(authentication.getAuthorities());
                    authorities.add(new SimpleGrantedAuthority(ChatRoles.Participant.toString()));
                    Authentication updatedAuthentication = new UsernamePasswordAuthenticationToken(
                            authentication.getPrincipal(),
                            authentication.getCredentials(),
                            authorities
                    );
                    SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);
                }
            }
        }
        chain.doFilter(request, response);
    }

    /** Вспомогательный метод чтобы  вытащить chatId из URI
    **/
    public static Long extractChatId(String requestURI) {
        Matcher matcher = chatIdPattern.matcher(requestURI);
        if (matcher.find()) {
            String chatIdStr = matcher.group(1);
            return Long.parseLong(chatIdStr);
        }
        return null;
    }
    
    private static final Pattern chatIdPattern = Pattern.compile("/chat/(\\d+)");
}
