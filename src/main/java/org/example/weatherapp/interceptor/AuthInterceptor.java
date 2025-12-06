package org.example.weatherapp.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.weatherapp.entity.Session;
import org.example.weatherapp.service.AuthService;
import org.example.weatherapp.service.SessionService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final SessionService sessionService;

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler
    ) {
        String path = request.getRequestURI();

        if (path.startsWith("/auth")) {
            return true;
        }

        Cookie[] cookies = getCookies(request, response);
        if (cookies == null) return false;

        String sessionId = getSessionId(cookies);
        if (sessionId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        Session session = sessionService.getById(sessionId);

        if (session == null || session.getExpiresAt().isBefore(LocalDateTime.now())) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        request.setAttribute("userId", session.getUser().getId());

        return true;
    }

    private Cookie[] getCookies(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
        return cookies;
    }

    private String getSessionId(Cookie[] cookies) {
        String sessionId = null;
        for (Cookie cookie : cookies) {
            if (AuthService.SESSION_COOKIE_NAME.equals(cookie.getName())) {
                sessionId = cookie.getValue();
            }
        }
        return sessionId;
    }

}

