package org.kamil.forwork.util.exceptions.email;

import org.kamil.forwork.util.exceptions.AnyCustomException;

public class EmailNotFoundException extends AnyCustomException {
    public EmailNotFoundException(String message) {
        super(message);
    }

    public EmailNotFoundException() {
    }
}