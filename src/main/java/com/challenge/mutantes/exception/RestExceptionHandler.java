package com.challenge.mutantes.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.RollbackException;
import java.rmi.UnexpectedException;


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(ResourceFormatException.class)
    protected ResponseEntity<Object> handleResourceFormatException(
            ResourceFormatException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(UnexpectedException.class)
    protected ResponseEntity<Object> handleUnexpectedException(
            UnexpectedException ex) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(value = {RollbackException.class, IllegalArgumentException.class, IllegalStateException.class,
            ConstraintViolationException.class, Exception.class})
    protected ResponseEntity<Object> handleGeneralException(
            RuntimeException ex) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        return buildResponseEntity(apiError);
    }
}
