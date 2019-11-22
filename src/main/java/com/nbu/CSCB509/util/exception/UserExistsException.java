package com.nbu.CSCB509.util.exception;

public class UserExistsException extends BaseException {
    public UserExistsException() {
        super("User already exists");
    }
}
