package org.kamil.forwork.util.exceptions.phoneNumber;

import org.kamil.forwork.util.exceptions.AnyCustomException;

public class PhoneNumberNotCreatedException extends AnyCustomException {
    public PhoneNumberNotCreatedException(String message) {
        super(message);
    }

    public PhoneNumberNotCreatedException() {
    }
}