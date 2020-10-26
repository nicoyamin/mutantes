package com.challenge.mutantes.controller;

import com.challenge.mutantes.exception.ResourceFormatException;
import com.challenge.mutantes.model.HumanRequest;
import com.challenge.mutantes.service.HumanService;
import com.challenge.mutantes.utilities.MutantUtils;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Histogram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class HumanController implements HumanApi {


    CollectorRegistry registry;
    private HumanService humanService;
    private final Counter countMutantDna;
    private final Counter countHumanDna;
    private final Histogram singleRequestLatency;
    private final Histogram multipleRequestLatency;


    @Autowired
    public HumanController(HumanService humanService, CollectorRegistry registry) {
        this.humanService = humanService;
        countMutantDna = Counter.build().name("count_mutant_dna").help("Mutants detected").register(registry);
        countHumanDna = Counter.build().name("count_human_dna").help("Humans detected").register(registry);
        singleRequestLatency = Histogram.build().name("single_dna_analysis_duration").help("How long it takes for a single DNA to be analyzed").register(registry);
        multipleRequestLatency = Histogram.build().name("multiple_dna_analysis_duration").help("How long it takes for multiple DNAs to be analyzed").register(registry);
    }

    @Override
    public ResponseEntity<Boolean> isMutant(@Valid HumanRequest request) throws ResourceFormatException {

        Histogram.Timer timer = singleRequestLatency.startTimer();

        boolean isMutant = humanService.isMutant(request.getDna());

        if(isMutant) {
            timer.observeDuration();
            countMutantDna.inc();
           return ResponseEntity.ok(isMutant);
        } else {
            timer.observeDuration();
            countHumanDna.inc();
            return new ResponseEntity<>(isMutant, HttpStatus.FORBIDDEN);
        }

    }

    @Override
    public ResponseEntity<String> testRandomDna(@Valid int dnaAmount, @Valid int dnaSize) throws ResourceFormatException {
        Histogram.Timer timer = multipleRequestLatency.startTimer();
        int mutantCount = 0;
        int humanCount = 0;

        for(int i=0; i<dnaAmount; i++) {
            boolean isMutant = humanService.isMutant(MutantUtils.generateDnaSequence(dnaSize));
            if (isMutant) {
                mutantCount++;
                countMutantDna.inc();
            } else {
                humanCount++;
                countHumanDna.inc();
            }
        }
        String testResults = new StringBuilder()
                .append(dnaAmount)
                .append(" DNAs were tested, out of which ")
                .append(mutantCount)
                .append(" are mutant and ")
                .append(humanCount)
                .append(" are human.").toString();

        timer.observeDuration();
        return ResponseEntity.ok(testResults);
    }

}
