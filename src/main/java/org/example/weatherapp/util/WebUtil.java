package org.example.weatherapp.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class WebUtil {

    public static final String SESSION_COOKIE_NAME = "SESSION_ID";
    private static final int ONE_HOUR_IN_SECONDS = 60 * 60;

    public static String getSessionIdFromCookies(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (SESSION_COOKIE_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        throw new RuntimeException("SessionId not found");
    }

    public static void setSessionCookie(UUID sessionId, HttpServletResponse response) {
        Cookie cookie = new Cookie(SESSION_COOKIE_NAME, sessionId.toString());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(ONE_HOUR_IN_SECONDS);

        response.addCookie(cookie);
    }

    public static void deleteSessionCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(SESSION_COOKIE_NAME, "");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");

        response.addCookie(cookie);
    }

}
