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
//        boolean isMutant= processDna(dnaMatrix);

        boolean isMutant = true;

        Human human = new Human();
        human.setDna(dna);
        human.setIsMutant(isMutant);

        humanRepository.save(human);

        return isMutant;
    }

    private static Multimap<Character, Point> validateDna(String[] dna) throws ResourceFormatException {

        StringBuilder stringBuilder = new StringBuilder();
        int totalMatches = 0;
        Set<Point> diagMatches = new HashSet<>();
        Set<Point> horMatches = new HashSet<>();

        HashMap<Character, MutableValueGraph> graphMap = new HashMap<>();


        if(dna.length < PATTERN_LENGTH) {
            throw new ResourceFormatException(DNA_SEQUENCE_TOO_SHORT_EXCEPTION);
        }
        int strandLength =  dna.length;

        //Step 1 - Convert dna sequence to a matrix for traversal
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

                MutableValueGraph<Point, Double> pointGraph = graphMap.get(currentBase);
                Point newBase = new Point(row,column);

                if(pointGraph == null) {
                    MutableValueGraph<Point, Double> weightedGraph = ValueGraphBuilder.undirected().build();
                    weightedGraph.addNode(newBase);
                    graphMap.put(currentBase, weightedGraph);
                } else {
                    Set<Point> nodeSet = pointGraph.nodes();
                    pointGraph.addNode(newBase);
                    for(Point node : nodeSet) {
                        if(!newBase.equals(node)) pointGraph.putEdgeValue(node, newBase, node.distance(newBase));
                    }
                }
                dnaMatrix.put(currentBase, newBase);
                }
            }
        return dnaMatrix;
    }



//    private static boolean processDna(Multimap dna) {
//
//        int detectedPattern = 0;
//        Point previousPatternStart = null;
//        Point previousPatternEnd = null;
//
//        //Step 2 - Traverse DNA matrix horizontally
//        for(int row = 0; row < dna.length; row++) {
//            for(int column = 0; column < dna.length; column++) {
//                //Step 3 - Check every direction while standing in each cell
//                for(int direction = 0; direction < 8; direction++) {
//                    int rowDirection = row + ROWS[direction];
//                    int columnDirection = column + COLUMNS[direction];
//                    int matches;
//
//                    for(matches = 1; matches < PATTERN_LENGTH; matches++) {
//                        //Check if out of bounds in current direction
//                        if(rowDirection >= dna.length || rowDirection < 0
//                                || columnDirection >= dna.length || columnDirection < 0)
//                            break;
//
//                        if(dna[row][column] != dna[rowDirection][columnDirection])
//                            break;
//
//                        rowDirection += ROWS[direction];
//                        columnDirection += COLUMNS[direction];
//                    }
//                    //Step 4 - Check if we have a matching pattern
//                    if (matches == PATTERN_LENGTH) {
//
//                        int finalRow = rowDirection-ROWS[direction];
//                        int finalColumn = columnDirection-COLUMNS[direction];
//
//                        //We need to check whether this match is just a previously found one but with its direction reversed
//                        if(detectedPattern == 0) {
//                            previousPatternStart = new Point(row,column);
//                            previousPatternEnd = new Point(finalRow, finalColumn);
//                            detectedPattern++;
//                            System.out.println("Pattern detected at (" + row +" ,"+column+")(" + finalRow+" ,"+ finalColumn +")" );
//                            break;
//                        }
//
//                        Point currentPatternStart = new Point(row, column);
//                        Point currentPatternEnd = new Point(finalRow, finalColumn);
//                        if(currentPatternStart.equals(previousPatternEnd) && currentPatternEnd.equals(previousPatternStart)) {
//                            System.out.println("Duplicate pattern at (" + row +" ,"+column+")(" + finalRow+" ,"+ finalColumn +")" );
//                            break;
//                        }
//
//                        detectedPattern++;
//                        System.out.println("Pattern detected at (" + row +" ,"+column+")(" + finalRow+" ,"+ finalColumn +")" );
//                    }
//                    //Step 5 - If we hit at least two matches, input is mutant DNA
//                    if (detectedPattern == MUTANT_BASES)
//                        return true;
//                }
//
//            }
//        }
//
//        //Step 5 - if we don't have at least two matches, input is human DNA
//        return false;
//    }
}
