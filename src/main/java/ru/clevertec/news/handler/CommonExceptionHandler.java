package ru.clevertec.news.handler;

import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.clevertec.news.exception.EntityNotFoundException;
import ru.clevertec.news.exception.ExceptionInformation;
import ru.clevertec.news.exception.ValidationException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {
    @ExceptionHandler(ServletException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ExceptionInformation handleServletException(ServletException exception) {
        return new ExceptionInformation(
                INTERNAL_SERVER_ERROR,
                INTERNAL_SERVER_ERROR.value(),
                "Houston, we have a problem");
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ExceptionInformation handleValidationException(ValidationException exception) {
        return new ExceptionInformation(BAD_REQUEST, BAD_REQUEST.value(), exception.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ExceptionInformation handleEntityNotFoundException(EntityNotFoundException exception) {
        return new ExceptionInformation(NOT_FOUND, NOT_FOUND.value(), exception.getMessage());
    }
}
