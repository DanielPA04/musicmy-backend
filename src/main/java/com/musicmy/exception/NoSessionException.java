package com.musicmy.exception;


public class NoSessionException extends RuntimeException{
    public NoSessionException(String mensaje){
        super(mensaje);
    }
}