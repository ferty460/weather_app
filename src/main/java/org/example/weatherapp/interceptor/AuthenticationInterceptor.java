package org.example.weatherapp.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.weatherapp.entity.Session;
import org.example.weatherapp.service.SessionService;
import org.example.weatherapp.util.WebUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    public static final String CURRENT_USER_ATTR = "currentUser";

    private final SessionService sessionService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            String sessionId = WebUtil.getSessionIdFromCookies(request.getCookies());
            Session session = sessionService.getById(sessionId);

            request.setAttribute(CURRENT_USER_ATTR, session.getUser());
        } catch (Exception ignored) {

        }

        return true;
    }

}
