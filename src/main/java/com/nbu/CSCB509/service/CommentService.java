package com.nbu.CSCB509.service;

import com.nbu.CSCB509.model.dto.ShowCommentDto;
import com.nbu.CSCB509.model.entity.Comment;
import com.nbu.CSCB509.repository.CommentRepository;
import com.nbu.CSCB509.util.SuccessMessage;
import com.nbu.CSCB509.util.exception.InvalidCommentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    private final UserService userService;

    private final PostService postService;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserService userService, @Lazy PostService postService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.postService = postService;
    }

    public SuccessMessage deleteById(long commentId, HttpServletResponse response) throws IOException {
        commentRepository.deleteById(commentId);
        response.sendRedirect("http://localhost:9999/posts.html");
        return new SuccessMessage("Comment deleted", LocalDate.now());
    }

    public void deleteCommentsByPostId(Long postId){
        commentRepository.deleteAllByPostId(postId);
    }

    public long getTotalComments(){
        return commentRepository.count();
    }

    public SuccessMessage getDistinctComments(long postId){
        int count = commentRepository.getDistinctCommentsByPostId(postId);
        return new SuccessMessage("" + count, LocalDate.now());
    }

    public List<ShowCommentDto> getAllCommentsForPost(long id){
        return commentRepository.findAllByPostId(id)
                .stream()
                .map(comment -> new ShowCommentDto(comment.getId(), comment.getComment(), comment.getPost().getId(), userService.getUserById(comment.getUser().getId())))
                .collect(Collectors.toList());
    }

    public SuccessMessage putCommentOnPost(long postId, long userId, HttpServletRequest request, HttpServletResponse response) throws InvalidCommentException, IOException {
        String comment = request.getParameter("comment");
        if (comment.isEmpty() || comment.equals(" ") || comment.equals("\n")) {
            throw new InvalidCommentException();
        }

        Comment com = new Comment();
        com.setComment(comment);
        com.setPost(postService.findPostById(postId));
        com.setUser(userService.findUserById(userId));
        commentRepository.save(com);

        response.sendRedirect("http://localhost:9999/posts.html");
        return new SuccessMessage("Comment added successfully", LocalDate.now());

    }

}
