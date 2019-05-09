package com.company.Exception;

public class ISBNInvalidException extends Exception {
    protected ISBNInvalidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ISBNInvalidException(Throwable cause) {
        super(cause);
    }

    public ISBNInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public ISBNInvalidException(String message) {
        super(message);
    }

    public ISBNInvalidException() {
        super();
    }
}
