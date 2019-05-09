package com.company.Exception;

public class PriceInvalidException extends Exception {
    public PriceInvalidException() {
        super();
    }

    protected PriceInvalidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public PriceInvalidException(Throwable cause) {
        super(cause);
    }

    public PriceInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public PriceInvalidException(String message) {
        super(message);
    }
}
