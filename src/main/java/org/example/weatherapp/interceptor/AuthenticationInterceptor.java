package org.example.weatherapp.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.weatherapp.entity.Session;
import org.example.weatherapp.service.SessionService;
import org.example.weatherapp.util.WebUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    public static final String CURRENT_USER_ATTR = "currentUser";

    private final SessionService sessionService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.trace("Authentication check for {}", request.getRequestURI());
        WebUtil.getSessionIdFromCookies(request.getCookies())
                .map(sessionService::getById)
                .map(Session::getUser)
                .ifPresent(user -> request.setAttribute(CURRENT_USER_ATTR, user));

        return true;
    }

}
