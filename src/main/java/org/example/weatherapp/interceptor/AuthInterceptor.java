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
public class AuthInterceptor implements HandlerInterceptor {

    public static final String CURRENT_USER_ATTR = "currentUser";

    private final SessionService sessionService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String path = request.getRequestURI();
        if (path.startsWith("/auth") || "/".equals(path) || "/error".equals(path)) {
            return true;
        }

        try {
            String sessionId = WebUtil.getSessionIdFromCookies(request.getCookies());
            Session session = sessionService.getById(sessionId);

            request.setAttribute(CURRENT_USER_ATTR, session.getUser());
            return true;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }

}
