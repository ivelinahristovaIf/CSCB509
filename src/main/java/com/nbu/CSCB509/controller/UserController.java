package com.nbu.CSCB509.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.nbu.CSCB509.service.PostService;
import com.nbu.CSCB509.service.UserService;
import com.nbu.CSCB509.util.SuccessMessage;
import com.nbu.CSCB509.util.exception.BaseException;
import com.nbu.CSCB509.util.exception.NotLoggedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nbu.CSCB509.model.dto.ShowUserDto;

@RestController
@RequestMapping(value = "/users")
public class UserController extends BaseController {

    private final PostService postService;

    private final UserService userService;

    @Autowired
    public UserController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @PostMapping(value = "/register")
    public SuccessMessage register(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws BaseException, IOException {
        return userService.register(session, request, response);
    }

    @PostMapping(value = "/login")
    public ShowUserDto login(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws BaseException, IOException {
        return userService.login(session, request, response);
    }

    @PostMapping(value = "/logout")
    public SuccessMessage logoutUser(HttpSession session, HttpServletResponse response) throws NotLoggedException, IOException {
        validateLogin(session, response);
        return userService.logout(session, response);
    }

    @GetMapping(value = "/profile")
    public ShowUserDto getUser(HttpSession session, HttpServletResponse response) throws NotLoggedException, IOException {
        validateLogin(session, response);
        return userService.getUser(session);
    }

    @GetMapping(value = "/donates/{id}")
    public SuccessMessage getDonatesGatheres(@PathVariable("id") Long userId) throws SQLException {
        return postService.getTotalDonates(userId);
    }

}
