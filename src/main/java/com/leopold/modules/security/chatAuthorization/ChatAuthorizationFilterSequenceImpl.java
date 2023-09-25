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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatAuthorizationFilterSequenceImpl implements FilterSequence {
    private final ChatService chatService;
    public ChatAuthorizationFilterSequenceImpl(ChatService chatService) {
        this.chatService = chatService;
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
        Long chatId = extractChatId(httpServletRequest.getRequestURI());
        List<GrantedAuthority> chatAuthorities = new ArrayList<>();
        if (userId != null && chatId != null) {
            boolean userInChat = chatService.checkUserInChat(chatId, userId);
            if (userInChat) {
                SimpleGrantedAuthority participant = new SimpleGrantedAuthority(ChatRoles.Participant.toString());
                chatAuthorities.add(participant);
            }
        }
        context.put(ComposerContextEnum.ChatAuthorities, chatAuthorities);
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
