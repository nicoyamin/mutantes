package com.challenge.mutantes.service;

import com.challenge.mutantes.entity.Human;
import com.challenge.mutantes.exception.ResourceFormatException;
import com.challenge.mutantes.repository.HumanRepository;
import com.challenge.mutantes.utilities.Direction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class HumanService {

    @Autowired
    HumanRepository humanRepository;

    static final List<Character> NITROGENOUS_BASES = Arrays.asList('A', 'T', 'C', 'G');
    static final int PATTERN_LENGTH = 4;
    static final String DNA_SEQUENCE_TOO_SHORT_EXCEPTION = "Dna base length and bases amount should be at least 4";
    static final String INCORRECT_BASE_EXCEPTION = " is not a DNA base. Nitrogenous bases should be A, T, C or G";
    static final String INCONSISTENT_DNA_SEQUENCE_EXCEPTION = "Dna sequences length must be equal to the sequences amount";



    public HumanService(HumanRepository humanRepository) {
        this.humanRepository = humanRepository;
    }

    @Transactional
    public boolean isMutant(String[] dna) throws ResourceFormatException {

        Boolean existingHuman = humanRepository.findHumanByDna(dna);

        if(existingHuman != null) {
            return existingHuman;
        }

        char[][] dnaMatrix = validateDna(dna);
        boolean isMutant= processDna(dnaMatrix);

        Human human = new Human();
        human.setDna(dna);
        human.setIsMutant(isMutant);

        humanRepository.save(human);

        return isMutant;
    }

    private static char[][] validateDna(String[] dna) throws ResourceFormatException {

        if(dna.length < PATTERN_LENGTH) {
            throw new ResourceFormatException(DNA_SEQUENCE_TOO_SHORT_EXCEPTION);
        }
        int strandLength =  dna.length;

        //Step 1 - Convert dna sequence to matrix
        char[][] dnaMatrix = new char[strandLength][strandLength];

        for(int row = 0; row < strandLength; row++) {
            if(dna[row].length() != strandLength) {
                throw new ResourceFormatException(INCONSISTENT_DNA_SEQUENCE_EXCEPTION);
            }
            for(int column = 0; column < strandLength; column++) {
                char currentBase = Character.toUpperCase(dna[row].charAt(column));
                if(!NITROGENOUS_BASES.contains(currentBase)) {
                    throw new ResourceFormatException(currentBase + INCORRECT_BASE_EXCEPTION);
                }
                dnaMatrix[row][column] = currentBase;
            }
        }
        return dnaMatrix;
    }


    private static boolean processDna(char[][] dna) {

        List<Point> previousMatch = new ArrayList<>();

        //Step 2 -  Start traversing the matrix horizontally
        for(int row = 0; row < dna.length; row++) {
            for(int column = 0; column < dna.length; column++) {
                for (Direction direction : Direction.values()) {
        //Step 3 - Check the foru required direction
                    List<Point> pattern = checkDirection(new Point(row, column), dna, direction);
         //Step 4 - If pattern is at least size 4, we have a possible match. We should check if it's not a duplicated pattern
         //or a subpattern of an existing larger  one.
                    if(pattern.size() >= PATTERN_LENGTH) {
                        if(previousMatch.isEmpty()) {
                            previousMatch = pattern;
                        }  else if(!previousMatch.containsAll(pattern)) {
        //Step 5 - If we hit two patterns, then DNA is mutant.
                            return true;
                        }
                    }
                }

            }

            }

    //Step 5 - if we don't have at least two matches, input is human DNA
        return false;
    }

    private static List<Point> checkDirection(Point basepoint, char[][] dna, Direction direction) {
        List<Point> pattern = new ArrayList<Point>();
        Point nextPoint = new Point(basepoint.x, basepoint.y);
        boolean inBounds = true;

        while(inBounds && dna[basepoint.x][basepoint.y] == dna[nextPoint.x][nextPoint.y]) {
            pattern.add(new Point(nextPoint.x, nextPoint.y));
            nextPoint.setLocation(nextPoint.x + direction.label.x, nextPoint.y + direction.label.y);
            inBounds = nextPoint.x >= 0 && nextPoint.y >= 0 && nextPoint.x < dna.length && nextPoint.y < dna.length;
        }

        return pattern;
    }
}
