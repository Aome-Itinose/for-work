package org.kamil.forwork.util.exceptions.email;

import org.kamil.forwork.util.exceptions.AnyCustomException;

public class EmailNotChangedException extends AnyCustomException {
    public EmailNotChangedException(String message) {
        super(message);
    }

    public EmailNotChangedException() {
    }
}