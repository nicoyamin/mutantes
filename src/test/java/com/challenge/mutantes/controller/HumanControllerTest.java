package com.challenge.mutantes.controller;

import com.challenge.mutantes.exception.ResourceFormatException;
import com.challenge.mutantes.model.HumanRequest;
import com.challenge.mutantes.service.HumanService;
import io.prometheus.client.CollectorRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.Invocation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class HumanControllerTest {

    @Mock
    HumanService service;

    @Mock
    CollectorRegistry registry;


    HumanController controller;

    HumanRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        request = new HumanRequest();
        request.setDna(new String[0]);
        controller= new HumanController(service,registry);
    }

    @Test
    public void shouldTestDnaSequenceAndReturnTrueAnd200Response() throws ResourceFormatException {
        when(service.isMutant(any(String[].class))).thenReturn(true);


        ResponseEntity response = controller.isMutant(request);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), true);
    }

    @Test
    public void shouldTestDnaSequenceAndReturnFalseAnd403Response() throws ResourceFormatException {
        when(service.isMutant(any(String[].class))).thenReturn(false);


        ResponseEntity response = controller.isMutant(request);

        assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);
        assertEquals(response.getBody(), false);
    }

    @Test
    public void shouldThrowResourceFormatExceptionOnIsMutant() throws ResourceFormatException {

        when(service.isMutant(any(String[].class))).thenThrow(ResourceFormatException.class);

        assertThrows(ResourceFormatException.class, () -> {
            controller.isMutant(request);
        });
    }

    @Test
    public void shouldTestDnaSequencesAndFindAllMutantsAndReturn200Response() throws ResourceFormatException {
        when(service.isMutant(any(String[].class))).thenReturn(true);
        int dnaAmount = 10;
        int dnaSize = 5;

        String testResults = new StringBuilder()
                .append(dnaAmount)
                .append(" DNAs were tested, out of which ")
                .append(dnaAmount)
                .append(" are mutant and ")
                .append(0)
                .append(" are human.").toString();

        ResponseEntity results = controller.testRandomDna(dnaAmount, dnaSize);

        Collection<Invocation> invocations = Mockito.mockingDetails(service).getInvocations();
        int numberOfCalls = invocations.size();

        assertEquals(dnaAmount, numberOfCalls);
        assertEquals(results.getBody(), testResults);

    }

    @Test
    public void shouldTestDnaSequencesAndFindAllHumansAndReturn200Response() throws ResourceFormatException {
        when(service.isMutant(any(String[].class))).thenReturn(false);
        int dnaAmount = 10;
        int dnaSize = 5;

        String testResults = new StringBuilder()
                .append(dnaAmount)
                .append(" DNAs were tested, out of which ")
                .append(0)
                .append(" are mutant and ")
                .append(dnaAmount)
                .append(" are human.").toString();

        ResponseEntity results = controller.testRandomDna(dnaAmount, dnaSize);

        Collection<Invocation> invocations = Mockito.mockingDetails(service).getInvocations();
        int numberOfCalls = invocations.size();

        assertEquals(dnaAmount, numberOfCalls);
        assertEquals(results.getBody(), testResults);

    }

    @Test
    public void shouldThrowResourceFormatExceptionOnTestRandomDna() throws ResourceFormatException {

        when(service.isMutant(any(String[].class))).thenThrow(ResourceFormatException.class);

        assertThrows(ResourceFormatException.class, () -> {
            controller.testRandomDna(10, 10);
        });
    }
}