package com.nbu.CSCB509.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.nbu.CSCB509.model.entity.User;
import com.nbu.CSCB509.service.CommentService;
import com.nbu.CSCB509.util.SuccessMessage;
import com.nbu.CSCB509.util.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nbu.CSCB509.model.dto.ShowCommentDto;

@RestController
@RequestMapping(value = "/comments")
public class CommentController extends BaseController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping(value = "/{id}")
    public List<ShowCommentDto> getCommentsForPost(@PathVariable("id") Long postId) {
        return commentService.getAllCommentsForPost(postId);
    }

    @PostMapping(value = "/{id}")
    public SuccessMessage
    putCommentOnPost(@PathVariable("id") Long postId, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws BaseException, IOException {
        validateLogin(session, response);
        User user = (User) session.getAttribute("user");
        return commentService.putCommentOnPost(postId, user.getId(), request, response);
    }

    @GetMapping(value = "/distinct/{id}")
    public SuccessMessage getDistinctComments(@PathVariable("id") Long postId) {
        return commentService.getDistinctComments(postId);
    }

    @DeleteMapping(value = "/{id}")
    public SuccessMessage deleteComment(@PathVariable("id") Long commentId, HttpServletResponse response) throws IOException {
        return commentService.deleteById(commentId, response);
    }

}
