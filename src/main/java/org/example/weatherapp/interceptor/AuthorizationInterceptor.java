package org.example.weatherapp.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        log.trace("Authorization check for {}", request.getRequestURI());
        if (request.getAttribute(AuthenticationInterceptor.CURRENT_USER_ATTR) == null) {
            response.sendRedirect("/auth/login");
            return false;
        }

        return true;
    }

}
