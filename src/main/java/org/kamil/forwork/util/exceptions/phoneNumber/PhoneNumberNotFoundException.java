package org.kamil.forwork.util.exceptions.phoneNumber;

import org.kamil.forwork.util.exceptions.AnyCustomException;

public class PhoneNumberNotFoundException extends AnyCustomException {
    public PhoneNumberNotFoundException(String message) {
        super(message);
    }

    public PhoneNumberNotFoundException() {
    }
}