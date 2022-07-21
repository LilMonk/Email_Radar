package io.spamradar.bootstrap.exception;

public class EmailParseException extends Exception {
    public EmailParseException(String message) {
        super(message);
    }

    public EmailParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailParseException(Throwable cause) {
        super(cause);
    }
}
