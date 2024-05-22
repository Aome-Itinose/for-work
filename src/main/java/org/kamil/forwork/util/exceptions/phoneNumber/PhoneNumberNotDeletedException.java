package org.kamil.forwork.util.exceptions.phoneNumber;

import org.kamil.forwork.util.exceptions.AnyCustomException;

public class PhoneNumberNotDeletedException extends AnyCustomException {
    public PhoneNumberNotDeletedException(String message) {
        super(message);
    }

    public PhoneNumberNotDeletedException() {
    }
}