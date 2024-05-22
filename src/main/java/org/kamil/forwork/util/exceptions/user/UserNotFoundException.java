package org.kamil.forwork.util.exceptions.user;

import org.kamil.forwork.util.exceptions.AnyCustomException;

public class UserNotFoundException extends AnyCustomException {
    public UserNotFoundException(String message){
        super(message);
    }

    public UserNotFoundException(){
        super("User not found");
    }
}
