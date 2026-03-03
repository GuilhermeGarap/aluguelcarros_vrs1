package com.aluguelcarros_vrs1.infra.exception;

import java.util.Collections;
import java.util.List;

public class ErrorDetailsException extends RuntimeException {

    private final List<String> messages;

    public ErrorDetailsException(String message) {
        super(message);
        this.messages = Collections.singletonList(message);
    }

    public ErrorDetailsException(List<String> messages) {
        super(String.join(" , ", messages));
        this.messages = messages;
    }

    public List<String> getMessages() {
        return messages;
    }
}