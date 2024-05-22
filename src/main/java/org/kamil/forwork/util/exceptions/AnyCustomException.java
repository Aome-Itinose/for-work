package org.kamil.forwork.util.exceptions;

public class AnyCustomException extends RuntimeException{
    public AnyCustomException(String message){
        super(message);
    }
    public AnyCustomException(){
        super();
    }
}
