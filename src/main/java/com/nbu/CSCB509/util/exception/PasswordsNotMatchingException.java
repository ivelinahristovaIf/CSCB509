package com.nbu.CSCB509.util.exception;

public class PasswordsNotMatchingException extends BaseException {
    public PasswordsNotMatchingException() {
        super("Passwords does not match");
    }
}
