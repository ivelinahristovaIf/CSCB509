package com.nbu.CSCB509.controller;

import com.nbu.CSCB509.util.ErrorMessage;
import com.nbu.CSCB509.util.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;

@RestController
public abstract class BaseController {
    @ExceptionHandler({UserExistsException.class, MissingValuableFieldsException.class, PasswordsNotMatchingException.class, WrongCredentialsException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage duplicateUser(Exception e) {
        return new ErrorMessage(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDate.now());
    }

    @ExceptionHandler(BaseException.class)
    @ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorMessage baseException(Exception e) {
        return new ErrorMessage(e.getMessage(), HttpStatus.METHOD_NOT_ALLOWED.value(), LocalDate.now());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage exception(Exception e) {
        return new ErrorMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDate.now());
    }

    void validateLogin(HttpSession session, HttpServletResponse response) throws NotLoggedException, IOException {
        if (session.getAttribute("user") == null) {
            response.sendRedirect("http://localhost:9999/login.html");
            throw new NotLoggedException();
        }
    }
}
