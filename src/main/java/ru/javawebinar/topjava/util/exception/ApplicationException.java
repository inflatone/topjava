package ru.javawebinar.topjava.util.exception;


import org.springframework.http.HttpStatus;

import java.util.Arrays;

public class ApplicationException extends RuntimeException {
    private final ErrorType type;
    private final String messageCode;
    private final HttpStatus status;
    private final String[] args;

    public ApplicationException(String messageCode, HttpStatus status) {
        this(ErrorType.APP_ERROR, messageCode, status);
    }

    public ApplicationException(ErrorType type, String messageCode, HttpStatus status, String... args) {
        super(String.format("type=%s, messageCode=%s, args=%s", type, messageCode, Arrays.toString(args)));
        this.type = type;
        this.messageCode = messageCode;
        this.status = status;
        this.args = args;
    }

    public ErrorType getType() {
        return type;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String[] getArgs() {
        return args;
    }
}
