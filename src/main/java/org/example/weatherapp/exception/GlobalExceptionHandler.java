package org.example.weatherapp.exception;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.weatherapp.exception.auth.InvalidCredentialsException;
import org.example.weatherapp.exception.auth.UserAlreadyExistsException;
import org.example.weatherapp.exception.auth.UserNotFoundException;
import org.example.weatherapp.exception.location.LocationAlreadyExistsException;
import org.example.weatherapp.exception.location.LocationNotFoundException;
import org.example.weatherapp.exception.session.SessionExpiredException;
import org.example.weatherapp.exception.session.SessionNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String ATTRIBUTE_NAME_ERROR = "error";
    public static final String ATTRIBUTE_NAME_CODE = "code";

    @ExceptionHandler(UserAlreadyExistsException.class)
    public String handleUserExists(UserAlreadyExistsException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(ATTRIBUTE_NAME_ERROR, ex.getMessage());
        return "redirect:/auth/registration";
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public String handleInvalidCredentials(InvalidCredentialsException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(ATTRIBUTE_NAME_ERROR, ex.getMessage());
        return "redirect:/auth/login";
    }

    @ExceptionHandler(SessionExpiredException.class)
    public String handleSessionExpired(SessionExpiredException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(ATTRIBUTE_NAME_ERROR, ex.getMessage());
        return "redirect:/auth/login";
    }

    @ExceptionHandler(LocationAlreadyExistsException.class)
    public String handleLocationExists(LocationAlreadyExistsException ex, HttpServletResponse response, Model model) {
        response.setStatus(HttpServletResponse.SC_CONFLICT);
        model.addAttribute(ATTRIBUTE_NAME_ERROR, ex.getMessage());
        model.addAttribute(ATTRIBUTE_NAME_CODE, HttpServletResponse.SC_CONFLICT);

        return "error";
    }

    @ExceptionHandler(LocationNotFoundException.class)
    public String handleLocationNotFound(LocationNotFoundException ex, HttpServletResponse response, Model model) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        model.addAttribute(ATTRIBUTE_NAME_ERROR, ex.getMessage());
        model.addAttribute(ATTRIBUTE_NAME_CODE, HttpServletResponse.SC_NOT_FOUND);

        return "error";
    }

    @ExceptionHandler(SessionNotFoundException.class)
    public String handleSessionNotFound(SessionNotFoundException ex, HttpServletResponse response, Model model) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        model.addAttribute(ATTRIBUTE_NAME_ERROR, ex.getMessage());
        model.addAttribute(ATTRIBUTE_NAME_CODE, HttpServletResponse.SC_UNAUTHORIZED);

        return "error";
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFound(UserNotFoundException ex, HttpServletResponse response, Model model) {
        log.error(ex.getMessage());
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        model.addAttribute(ATTRIBUTE_NAME_ERROR, ex.getMessage());
        model.addAttribute(ATTRIBUTE_NAME_CODE, HttpServletResponse.SC_NOT_FOUND);

        return "error";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNotFound(NoHandlerFoundException ex, HttpServletResponse response, Model model) {
        log.error("Page not found: {}", ex.getRequestURL());

        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        model.addAttribute(ATTRIBUTE_NAME_ERROR, "Page not found: " + ex.getRequestURL());
        model.addAttribute(ATTRIBUTE_NAME_CODE, HttpServletResponse.SC_NOT_FOUND);

        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, HttpServletResponse response, Model model) {
        log.error(ex.getMessage());
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        model.addAttribute(ATTRIBUTE_NAME_ERROR, ex.getMessage());
        model.addAttribute(ATTRIBUTE_NAME_CODE, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        return "error";
    }

}
