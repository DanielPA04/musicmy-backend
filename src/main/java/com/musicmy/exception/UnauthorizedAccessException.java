package com.musicmy.exception;



public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(String mensaje) {
        super(mensaje);
    }
}