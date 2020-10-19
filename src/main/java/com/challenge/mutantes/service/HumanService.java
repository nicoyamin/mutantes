package com.challenge.mutantes.service;

import org.springframework.stereotype.Service;

import java.awt.*;

@Service
public class HumanService {

    static final int[] ROWS = {-1,-1,-1,0,1,1,1,0};
    static final int[] COLUMNS = {-1,0,1,1,1,0,-1,-1};
    static final int PATTERN_LENGTH = 4;
    static final int MUTANT_STRANDS = 2;

    public HumanService() {
    }

    public boolean isMutant(String[] dna) {

        int strandLength =  dna.length;

        char[][] dnaMatrix = new char[strandLength][strandLength];

        //Step 1 - Convert dna sequence to a matrix for travesal
        for(int row = 0; row < strandLength; row++) {
            char[] dnaStrand = dna[row].toCharArray();
            for(int column = 0; column < strandLength; column++) {
                dnaMatrix[row][column] = dnaStrand[column];
            }
        }

        return processDnaMatrix(dnaMatrix);
    }

    private static boolean processDnaMatrix(char[][] dna) {

        int detectedPattern = 0;
        Point previousPatternStart = null;
        Point previousPatternEnd = null;

        //Step 2 - Traverse DNA matrix horizontally
        for(int row = 0; row < dna.length; row++) {
            for(int column = 0; column < dna.length; column++) {
                //Step 3 - Check every direction while standing in each cell
                for(int direction = 0; direction < 8; direction++) {
                    int rowDirection = row + ROWS[direction];
                    int columnDirection = column + COLUMNS[direction];
                    int matches;

                    for(matches = 1; matches < PATTERN_LENGTH; matches++) {
                        //Check if out of bounds in current direction
                        if(rowDirection >= dna.length || rowDirection < 0
                                || columnDirection >= dna.length || columnDirection < 0)
                            break;

                        if(dna[row][column] != dna[rowDirection][columnDirection])
                            break;

                        rowDirection += ROWS[direction];
                        columnDirection += COLUMNS[direction];
                    }
                    //Step 4 - Check if we have a matching pattern
                    if (matches == PATTERN_LENGTH) {

                        int finalRow = rowDirection-ROWS[direction];
                        int finalColumn = columnDirection-COLUMNS[direction];

                        //We need to check whether this match is just a previously found one but with its direction reversed
                        if(detectedPattern == 0) {
                            previousPatternStart = new Point(row,column);
                            previousPatternEnd = new Point(finalRow, finalColumn);
                            detectedPattern++;
                            System.out.println("Pattern detected at (" + row +" ,"+column+")(" + finalRow+" ,"+ finalColumn +")" );
                            break;
                        }

                        Point currentPatternStart = new Point(row, column);
                        Point currentPatternEnd = new Point(finalRow, finalColumn);
                        if(currentPatternStart.equals(previousPatternEnd) && currentPatternEnd.equals(previousPatternStart)) {
                            System.out.println("Duplicate pattern at (" + row +" ,"+column+")(" + finalRow+" ,"+ finalColumn +")" );
                            break;
                        }

                        detectedPattern++;
                        System.out.println("Pattern detected at (" + row +" ,"+column+")(" + finalRow+" ,"+ finalColumn +")" );
                    }
                    //Step 5 - If we hit at least two matches, input is mutant DNA
                    if (detectedPattern == MUTANT_STRANDS)
                        return true;
                }

            }
        }

        //Step 5 - if we don't have at least two matches, input human DNA
        return false;
    }
}
