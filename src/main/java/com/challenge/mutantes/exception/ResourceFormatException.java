package com.challenge.mutantes.exception;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResourceFormatException extends Exception {

    private List<String> errorMessages = new ArrayList<>();

    public ResourceFormatException(String msg) {
        super(msg);
    }

    public void addErrorMessage(String message) {
        this.errorMessages.add(message);
    }

}
