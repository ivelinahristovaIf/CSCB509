package com.nbu.CSCB509.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import com.nbu.CSCB509.model.entity.User;
import com.nbu.CSCB509.service.CategoryService;
import com.nbu.CSCB509.service.CommentService;
import com.nbu.CSCB509.service.PostService;
import com.nbu.CSCB509.service.UserService;
import com.nbu.CSCB509.util.SuccessMessage;
import com.nbu.CSCB509.util.exception.BaseException;
import com.nbu.CSCB509.util.exception.NotLoggedException;
import com.nbu.CSCB509.util.exception.PostHasIncomeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nbu.CSCB509.model.dto.CategoryDto;
import com.nbu.CSCB509.model.dto.GeneralInfoDto;
import com.nbu.CSCB509.model.dto.ShowPostDto;
import com.nbu.CSCB509.model.dto.ShowPostNoUserDto;

@RestController
@RequestMapping(value = "/posts", produces = {"application/json"})
public class PostController extends BaseController {

    private final PostService postService;

    private final UserService userService;

    private final CommentService commentService;

    private final CategoryService categoryService;

    @Autowired
    public PostController(PostService postService, UserService userService, CommentService commentService, CategoryService categoryService) {
        this.postService = postService;
        this.userService = userService;
        this.commentService = commentService;
        this.categoryService = categoryService;
    }

    @PostMapping
    public ShowPostDto addPost(HttpServletResponse response, HttpServletRequest request, HttpSession session) throws BaseException, ParseException, IOException {
        validateLogin(session, response);
        User user = (User) session.getAttribute("user");
        return postService.addPost(user, request, response);
    }

    @GetMapping(value = "/{id}")
    public List<ShowPostNoUserDto> getPosts(@PathVariable("id") Long userId, HttpSession session, HttpServletResponse response) throws NotLoggedException, IOException {
        validateLogin(session, response);
        return postService.getPostsForUser(userId);
    }

    @DeleteMapping(value = "/{id}")
    @Transactional
    public SuccessMessage deletePost(@PathVariable("id") Long postId, HttpServletResponse response, HttpSession session) throws PostHasIncomeException, IOException, NotLoggedException {
        validateLogin(session, response);
        return postService.deleteById(postId, response);
    }

    @GetMapping(value = "/top")
    public List<ShowPostNoUserDto> getTop5Posts(HttpServletResponse response, HttpServletRequest request) {
        return postService.getTop5Posts(response, request);
    }

    @GetMapping(value = "/all")
    public List<ShowPostNoUserDto> getAllPosts(HttpServletResponse response, HttpServletRequest request) {
        return postService.getAllPosts(response, request);
    }

    @GetMapping(value = "/categories")
    public List<CategoryDto> getCategories(HttpServletResponse response, HttpServletRequest request) {
        return categoryService.getCategories(response, request);
    }

    @PostMapping(value = "/donate/{id}")
    public SuccessMessage donateToPost(@PathVariable("id") Long postId, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException, NotLoggedException {
        validateLogin(session, response);
        return postService.takeDonation(postId, request, response);
    }

    @GetMapping(value = "/info")
    public GeneralInfoDto getInfo() {
        GeneralInfoDto generalInfoDto = new GeneralInfoDto();
        generalInfoDto.setUserCount(userService.getCount());
        generalInfoDto.setPostsCount(postService.getTotalPosts());
        generalInfoDto.setCommentsWritten(commentService.getTotalComments());
        return generalInfoDto;
    }

    @GetMapping(value = "/category/{id}")
    public List<ShowPostNoUserDto> getAllPosts(@PathVariable("id") Long categId, HttpServletResponse response, HttpServletRequest request) throws SQLException {
        return postService.getPostsPerCategory(categId, request, response);
    }

}