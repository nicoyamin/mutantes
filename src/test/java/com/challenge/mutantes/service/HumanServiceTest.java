package com.challenge.mutantes.service;

import com.challenge.mutantes.exception.ResourceFormatException;
import com.challenge.mutantes.repository.HumanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class HumanServiceTest {

    @Mock
    HumanRepository repository;

    HumanService service;

    String[] humanDna = new String[]{"GCAAAG","AAACAT","TATAAG","TTAAGT","ACAGCA","AGCCCC"};
    String[] mutantDna = new String[]{"ATGCGA","CAGTTC","TTATGT","AAAAGG","CCCCTA","TCACTG"};
    String[] mutantDnaDuplicatePattern = new String[] {"CCCCTC","CTAGTC","GTAATC","GTCGCC","ACGTGA","ACCTAT"};
    String[] shortDna = new String[]{"AAA", "TTT", "CCC"};
    String[] wrongDna = new String[]{"AAAA", "TTTT", "CCCC", "ZZZZ"};
    String[] inconsistentDna = new String[]{"CGGTTT","GCTATT","AGGTTT","GGGGCT","AATATG","ATCAG"};


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new HumanService(repository);
    }

    @Test
    public void shouldTestNewMutantDnaAndReturnTrue() throws ResourceFormatException {

        when(repository.findHumanByDna(any(String[].class))).thenReturn(null);

        boolean result = service.isMutant(mutantDna);

        assertEquals(true, result);
    }

    @Test
    public void shouldTestNewMutantDnaWithDuplicatePatternAndReturnTrue() throws ResourceFormatException {

        when(repository.findHumanByDna(any(String[].class))).thenReturn(null);

        boolean result = service.isMutant(mutantDnaDuplicatePattern);

        assertEquals(true, result);
    }

    @Test
    public void shouldTestNewHumanDnaAndReturnFalse() throws ResourceFormatException {

        when(repository.findHumanByDna(any(String[].class))).thenReturn(null);

        boolean result = service.isMutant(humanDna);

        assertEquals(false, result);
    }

    @Test
    public void shouldTestExistingMutantDnaAndReturnTrue() throws ResourceFormatException {

        when(repository.findHumanByDna(any(String[].class))).thenReturn(true);

        boolean result = service.isMutant(mutantDna);

        assertEquals(true, result);
    }

    @Test
    public void shouldTestExistingHumanDnaAndReturnFalse() throws ResourceFormatException {

        when(repository.findHumanByDna(any(String[].class))).thenReturn(false);

        boolean result = service.isMutant(mutantDna);

        assertEquals(false, result);
    }

    @Test
    public void shouldTestDnaAndThrowDnaSequenceTooShortException() {

        when(repository.findHumanByDna(any(String[].class))).thenReturn(null);

        ResourceFormatException exception = assertThrows(ResourceFormatException.class, () -> {
            service.isMutant(shortDna);
        });

        String expectedMessage = "Dna base length and bases amount should be at least 4";

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void shouldTestDnaAndThrowIncorrectBaseException() {

        when(repository.findHumanByDna(any(String[].class))).thenReturn(null);

        ResourceFormatException exception = assertThrows(ResourceFormatException.class, () -> {
            service.isMutant(wrongDna);
        });

        String expectedMessage = "Z is not a DNA base. Nitrogenous bases should be A, T, C or G";

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void shouldTestDnaAndThrowInconsistentDnaSequenceException() {

        when(repository.findHumanByDna(any(String[].class))).thenReturn(null);

        ResourceFormatException exception = assertThrows(ResourceFormatException.class, () -> {
            service.isMutant(inconsistentDna);
        });

        String expectedMessage = "Dna sequences length must be equal to the sequences amount";

        assertEquals(expectedMessage, exception.getMessage());
    }
}