package ru.javaops.topjava.util.exeption;

import java.util.Arrays;

public class ApplicationException extends RuntimeException {
    private final ErrorType type;
    private final String messageCode;
    private final String[] args;

    public ApplicationException(String messageCode) {
        this(ErrorType.APP_ERROR, messageCode);
    }
    public ApplicationException(ErrorType type, String messageCode, String... args) {
        super(String.format("type=%s, messageCode=%s, args=%s", type, messageCode, Arrays.toString(args)));
        this.type = type;
        this.messageCode = messageCode;
        this.args = args;
    }

    public ErrorType getType() {
        return type;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public String[] getArgs() {
        return args;
    }
}
