package ru.javawebinar.topjava.util.exception;

public class ErrorInfo {
    private final String url;
    private final ErrorType type;
    private final String typeMessage;
    private final String[] details;

    public ErrorInfo(CharSequence url, ErrorType type, String typeMessage, String... detail) {
        this.url = url.toString();
        this.type = type;
        this.typeMessage = typeMessage;
        this.details = detail;
    }
}
