package com.challenge.mutantes.utilities;


import org.apache.commons.lang3.RandomStringUtils;

public final class MutantUtils {


    public static String[] generateDnaSequence(int size) {
         //DNA sequences must be at least 4x4
        size = Math.max(size, 4);

        String[] dna = new String[size];

        for(int i=0; i < size; i++) {
           dna[i] = RandomStringUtils.random(size, "ATCG");

        }

        return dna;
    }

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
