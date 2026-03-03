package com.aluguelcarros_vrs1.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Component
public class JwtTokenFilter extends GenericFilterBean {

    @Autowired
    private JwtTokenProvider tokenProvider;

    public JwtTokenFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            var token = tokenProvider.resolveToken((HttpServletRequest) request);

            if (StringUtils.isNotBlank(token) && tokenProvider.validateToken(token)) {
                Authentication authentication = tokenProvider.getAuthentication(token);
                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            filterChain.doFilter(request, response);

        } catch (com.auth0.jwt.exceptions.TokenExpiredException e) {
            handleException((HttpServletResponse) response, "Token expirado: " + e.getMessage(), (HttpServletRequest) request);
        } catch (Exception e) {
            handleException((HttpServletResponse) response, "Erro na autenticação: " + e.getMessage(), (HttpServletRequest) request);
        }
    }

    private void handleException(HttpServletResponse response, String message, HttpServletRequest request) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        String cleanMessage = message;
        if (message.contains("The Token has expired on")) {
            String date = message.substring(message.lastIndexOf("on") + 3);
            cleanMessage = "Token expirado em: " + date;
        }

        String json = String.format(
                "{\"timestamp\": \"%s\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"%s\", \"path\": \"%s\"}",
                java.time.Instant.now(),
                cleanMessage.replace("\"", "\\\""),
                request.getRequestURI()
        );

        response.getWriter().write(json);
    }
}

