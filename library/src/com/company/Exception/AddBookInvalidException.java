package com.company.Exception;

public class AddBookInvalidException extends Exception {
    protected AddBookInvalidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public AddBookInvalidException(Throwable cause) {
        super(cause);
    }

    public AddBookInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public AddBookInvalidException(String message) {
        super(message);
    }

    public AddBookInvalidException() {
        super();
    }
}
