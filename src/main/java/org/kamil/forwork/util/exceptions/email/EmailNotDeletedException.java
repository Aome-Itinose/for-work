package org.kamil.forwork.util.exceptions.email;

import org.kamil.forwork.util.exceptions.AnyCustomException;

public class EmailNotDeletedException extends AnyCustomException {
    public EmailNotDeletedException(String message) {
        super(message);
    }

    public EmailNotDeletedException() {
    }
}