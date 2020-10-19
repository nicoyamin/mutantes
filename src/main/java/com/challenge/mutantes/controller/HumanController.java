package com.challenge.mutantes.controller;

import com.challenge.mutantes.model.HumanRequest;
import com.challenge.mutantes.service.HumanService;
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
    public ResponseEntity<Boolean> isMutant(@Valid HumanRequest request) {

        boolean isMutant = humanService.isMutant(request.getDna());

        if(isMutant) {
           return ResponseEntity.ok(isMutant);
        } else {
           return new ResponseEntity<>(isMutant, HttpStatus.FORBIDDEN);
        }

    }
}
