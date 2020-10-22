package com.challenge.mutantes.exception;

import java.util.ArrayList;
import java.util.List;

public class ResourceFormatException extends Exception {

    private List<String> errorMessages = new ArrayList<>();

    public ResourceFormatException() {
    }

    public ResourceFormatException(String msg) {
        super(msg);
    }

    /**
     * @return the errorMessages
     */
    public List<String> getErrorMessages() {
        return errorMessages;
    }

    /**
     * @param errorMessages the errorMessages to set
     */
    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public void addErrorMessage(String message) {
        this.errorMessages.add(message);
    }

}
