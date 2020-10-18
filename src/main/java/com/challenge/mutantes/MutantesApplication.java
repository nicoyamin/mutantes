package com.challenge.mutantes;

import com.challenge.mutantes.utilities.MutantUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.*;
import java.util.Random;

@SpringBootApplication
public class MutantesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MutantesApplication.class, args);

		for(int i=0; i< MutantUtils.getRandomNumber(50,100);i++){
			boolean isMutant = isMutant(MutantUtils.generateDnaSequence(MutantUtils.getRandomNumber(4,5)));
			System.out.println(isMutant);
		}

	}

	private static boolean isMutant(String[] dna) {

		int strandLength =  dna.length;

		char[][] dnaMatrix = new char[strandLength][strandLength];

		//Step 1 - Convert dna sequence to a matrix for travesal
		for(int row = 0; row < strandLength; row++) {
			char[] dnaStrand = dna[row].toCharArray();
			for(int column = 0; column < strandLength; column++) {
				dnaMatrix[row][column] = dnaStrand[column];
			}
		}

		for (int i = 0; i < dnaMatrix.length; i++) {
			for (int j = 0; j < dnaMatrix[i].length; j++) {
				System.out.print(dnaMatrix[i][j] + " ");
			}
			System.out.println();
		}
		return processDnaMatrix(dnaMatrix);
	}

	private static boolean processDnaMatrix(char[][] dna) {

		final int[] ROWS = {-1,-1,-1,0,1,1,1,0};
		final int[] COLUMNS = {-1,0,1,1,1,0,-1,-1};
		final int PATTERN_LENGTH = 4;
		final int MUTANT_STRANDS = 2;

		int detectedPattern = 0;
		Point previousPatternStart = null;
		Point previousPatternEnd = null;

		for(int row = 0; row < dna.length; row++) {
			for(int column = 0; column < dna.length; column++) {
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

					if (detectedPattern == MUTANT_STRANDS)
						return true;
				}

			}
		}
		return false;
	}


}
