package com.challenge.mutantes.controller;

import com.challenge.mutantes.exception.ResourceFormatException;
import com.challenge.mutantes.model.HumanRequest;
import com.challenge.mutantes.service.HumanService;
import com.challenge.mutantes.utilities.MutantUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class HumanController implements HumanApi {

    HumanService humanService;

    @Autowired
    public HumanController(HumanService humanService) {
        this.humanService = humanService;
    }

    @Override
    public ResponseEntity<Boolean> isMutant(@Valid HumanRequest request) throws ResourceFormatException {

        boolean isMutant = humanService.isMutant(request.getDna());

        if(isMutant) {
           return ResponseEntity.ok(isMutant);
        } else {
           return new ResponseEntity<>(isMutant, HttpStatus.FORBIDDEN);
        }

    }

    @Override
    public ResponseEntity<String> testRandomDna(@Valid int dnaAmount, @Valid int dnaSize) throws ResourceFormatException {

        int mutantCount = 0;
        int humanCount = 0;

        for(int i=0; i<dnaAmount; i++) {
            boolean isMutant = humanService.isMutant(MutantUtils.generateDnaSequence(dnaSize));
            if (isMutant) {
                mutantCount++;
            } else {
                humanCount++;
            }
        }
        String testResults = new StringBuilder(dnaAmount)
                .append(" DNAs were tested, out of which ")
                .append(mutantCount)
                .append(" are mutant and ")
                .append(humanCount)
                .append("are human.").toString();

        return ResponseEntity.ok(testResults);
    }

}
