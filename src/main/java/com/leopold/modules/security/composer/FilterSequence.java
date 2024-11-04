package com.leopold.modules.security.composer;

import com.leopold.modules.security.composer.context.ComposerContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import javax.naming.AuthenticationException;
import java.io.IOException;

public interface FilterSequence {
    void doFilter(ServletRequest request, ServletResponse response, ComposerContext context) throws ServletException, IOException, AuthenticationException;
}
