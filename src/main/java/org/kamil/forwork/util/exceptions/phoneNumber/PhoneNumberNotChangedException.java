package org.kamil.forwork.util.exceptions.phoneNumber;

import org.kamil.forwork.util.exceptions.AnyCustomException;

public class PhoneNumberNotChangedException extends AnyCustomException {
    public PhoneNumberNotChangedException(String message) {
        super(message);
    }

    public PhoneNumberNotChangedException() {
    }
}