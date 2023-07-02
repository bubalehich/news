package ru.clevertec.news.handler;

import jakarta.servlet.ServletException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.clevertec.news.exception.EntityNotFoundException;
import ru.clevertec.news.exception.ExceptionInformation;
import ru.clevertec.news.exception.ValidationException;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {
    @ExceptionHandler(ServletException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ExceptionInformation handleServletException(ServletException exception) {
        log.error(exception.getMessage());
        return new ExceptionInformation(
                INTERNAL_SERVER_ERROR,
                INTERNAL_SERVER_ERROR.value(),
                "Houston, we have a problem");
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ExceptionInformation handleValidationException(ValidationException exception) {
        log.info(exception.getMessage());
        return new ExceptionInformation(BAD_REQUEST, BAD_REQUEST.value(), exception.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ExceptionInformation handleEntityNotFoundException(EntityNotFoundException exception) {
        log.info(exception.getMessage());
        return new ExceptionInformation(NOT_FOUND, NOT_FOUND.value(), exception.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ExceptionInformation handleConstraintViolationException(ConstraintViolationException exception) {
        log.info(exception.getMessage());
        var message =
                exception.getConstraintViolations().stream()
                        .map(CommonExceptionHandler::getMessage)
                        .collect(Collectors.joining(", "));
        return new ExceptionInformation(BAD_REQUEST, BAD_REQUEST.value(), message);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    @ResponseStatus(BAD_REQUEST)
    public ExceptionInformation handleMethodArgumentNotValidException(BindException exception) {
        log.info(exception.getMessage());
        var message =
                exception.getBindingResult().getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.joining(", "));
        return new ExceptionInformation(BAD_REQUEST, BAD_REQUEST.value(), message);
    }

    protected static String getMessage(ConstraintViolation<?> constraintViolation) {
        var propertyPath = constraintViolation.getPropertyPath().toString();
        var propertyName =
                Arrays.stream(propertyPath.split("\\.")).skip(2).collect(Collectors.joining("."));
        return String.format(
                "%s: %s",
                isNotBlank(propertyName) ? propertyName : propertyPath, constraintViolation.getMessage());
    }
}
