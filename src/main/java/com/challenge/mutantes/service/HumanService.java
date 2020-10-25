package com.challenge.mutantes.service;

import com.challenge.mutantes.entity.Human;
import com.challenge.mutantes.exception.ResourceFormatException;
import com.challenge.mutantes.repository.HumanRepository;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

@Service
public class HumanService {

    @Autowired
    HumanRepository humanRepository;

    static final int[] ROWS = {-1,-1,-1,0,1,1,1,0};
    static final int[] COLUMNS = {-1,0,1,1,1,0,-1,-1};
    static final List<Character> NITROGENOUS_BASES = Arrays.asList('A', 'T', 'C', 'G');
    static final int PATTERN_LENGTH = 4;
    static final int MUTANT_BASES = 2;
    static final double DIAG_DISTANCE = 1.4142135623730951;
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

        Multimap dnaMatrix = validateDna(dna);
        boolean isMutant= processDna(dnaMatrix);

//        boolean isMutant = true;

        Human human = new Human();
        human.setDna(dna);
        human.setIsMutant(isMutant);

        //humanRepository.save(human);

        return isMutant;
    }

    private static Multimap<Character, Point> validateDna(String[] dna) throws ResourceFormatException {

        if(dna.length < PATTERN_LENGTH) {
            throw new ResourceFormatException(DNA_SEQUENCE_TOO_SHORT_EXCEPTION);
        }
        int strandLength =  dna.length;

        //Step 1 - Convert dna sequence to a multiMap to isolate similar bases
        Multimap<Character,Point> dnaMatrix = ArrayListMultimap.create();

        for(int row = 0; row < strandLength; row++) {
            if(dna[row].length() != strandLength) {
                throw new ResourceFormatException(INCONSISTENT_DNA_SEQUENCE_EXCEPTION);
            }
            for(int column = 0; column < strandLength; column++) {
                char currentBase = Character.toUpperCase(dna[row].charAt(column));
                if(!NITROGENOUS_BASES.contains(currentBase)) {
                    throw new ResourceFormatException(currentBase + INCORRECT_BASE_EXCEPTION);
                }
                Point newBase = new Point(row,column);
                dnaMatrix.put(currentBase, newBase);
            }
        }
        return dnaMatrix;
    }


    private static boolean processDna(Multimap<Character, Point> dna) {



        for(Character base : NITROGENOUS_BASES) {
            List<Point> points = new ArrayList<>(dna.get(base));
            for(int i=0; i < points.size(); i++) {
                Point point1 = points.get(i);

                for(int j=i+1; j < points.size()-i; j++) {
                    Point point2 = points.get(j);
                    if(point1.distance(point2) == 1.0 && point2.x-point1.x == 1 && point2.y-point1.y == 0) {

                    }
                }
            }

        }



        //Step 5 - if we don't have at least two matches, input is human DNA
        return false;
    }
}
