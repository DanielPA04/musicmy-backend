package com.musicmy.exception;

public class ErrorChangingPassword extends RuntimeException {
    public ErrorChangingPassword(String mensaje) {
        super(mensaje);
    }
}