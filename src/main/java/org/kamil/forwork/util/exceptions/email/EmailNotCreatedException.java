package org.kamil.forwork.util.exceptions.email;

import org.kamil.forwork.util.exceptions.AnyCustomException;

public class EmailNotCreatedException extends AnyCustomException {
    public EmailNotCreatedException(String message) {
        super(message);
    }

    public EmailNotCreatedException() {
    }
}