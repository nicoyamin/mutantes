package com.challenge.mutantes.controller;

import com.challenge.mutantes.exception.ResourceFormatException;
import com.challenge.mutantes.model.HumanRequest;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Api(value="human")
public interface HumanApi {
    @ApiOperation(value = "Check if human is mutant", nickname = "isMutant", response = Boolean.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Mutant detected"),
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 204, message = "Bad request"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Not a mutant"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    })
    @RequestMapping(value="/human/isMutant", method= RequestMethod.POST)
    ResponseEntity<Boolean> isMutant(@ApiParam(value="DNA sequence")
                                            @Valid @RequestBody(required=true) HumanRequest request) throws ResourceFormatException;

}
