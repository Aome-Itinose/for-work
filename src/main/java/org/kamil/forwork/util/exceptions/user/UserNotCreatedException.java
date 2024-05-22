package org.kamil.forwork.util.exceptions.user;

import org.kamil.forwork.util.exceptions.AnyCustomException;

public class UserNotCreatedException extends AnyCustomException {
    public UserNotCreatedException(String message) {
        super(message);
    }

    public UserNotCreatedException() {
    }
}
