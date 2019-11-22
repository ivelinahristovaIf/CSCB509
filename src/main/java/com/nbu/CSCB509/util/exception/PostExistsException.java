package com.nbu.CSCB509.util.exception;

public class PostExistsException extends BaseException {
    public PostExistsException() {
        super("Post already exists");
    }
}
