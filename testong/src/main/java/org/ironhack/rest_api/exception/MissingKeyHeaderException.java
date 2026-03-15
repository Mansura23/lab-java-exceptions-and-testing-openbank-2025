package org.ironhack.rest_api.exception;

public class MissingKeyHeaderException extends RuntimeException {
    public MissingKeyHeaderException(String message) {
        super(message);
    }
}
