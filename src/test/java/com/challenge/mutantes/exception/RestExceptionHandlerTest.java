package com.challenge.mutantes.exception;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.rmi.UnexpectedException;

import static org.junit.jupiter.api.Assertions.*;

class RestExceptionHandlerTest {

    RestExceptionHandler handler = new RestExceptionHandler();

    @Test
    public void shouldTestResourceFormatExceptionHandler() {
        ResourceFormatException exception = new ResourceFormatException("test exception");
        exception.addErrorMessage("test exception 2");

        ResponseEntity<Object> returnedResponse = handler.handleResourceFormatException(exception);

        assertEquals(returnedResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(returnedResponse.getBody().getClass(), ApiError.class);
    }

    @Test
    public void shouldTestUnexpectedExceptionHandler() {
        UnexpectedException exception = new UnexpectedException("");

        ResponseEntity<Object> returnedResponse = handler.handleUnexpectedException(exception);

        assertEquals(returnedResponse.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        assertEquals(returnedResponse.getBody().getClass(), ApiError.class);
    }

    @Test
    public void shouldTestGeneralExceptionHandler() {
        RuntimeException exception = new RuntimeException("test exception");

        ResponseEntity<Object> returnedResponse = handler.handleGeneralException(exception);

        assertEquals(returnedResponse.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        assertEquals(returnedResponse.getBody().getClass(), ApiError.class);
    }
}