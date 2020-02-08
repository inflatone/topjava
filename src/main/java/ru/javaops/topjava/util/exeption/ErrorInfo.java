package ru.javaops.topjava.util.exeption;

public class ErrorInfo {
    private final String url;
    private final ErrorType type;
    private final String details[];

    public ErrorInfo(CharSequence url, ErrorType type, String... details) {
        this.url = url.toString();
        this.type = type;
        this.details = details;
    }
}