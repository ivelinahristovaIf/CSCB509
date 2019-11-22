package com.nbu.CSCB509.service;

import com.nbu.CSCB509.model.dto.ShowPostDto;
import com.nbu.CSCB509.model.dto.ShowPostNoUserDto;
import com.nbu.CSCB509.model.dto.ShowUserDto;
import com.nbu.CSCB509.model.entity.Category;
import com.nbu.CSCB509.model.entity.Post;
import com.nbu.CSCB509.model.entity.User;
import com.nbu.CSCB509.repository.PostRepository;
import com.nbu.CSCB509.util.SuccessMessage;
import com.nbu.CSCB509.util.exception.BaseException;
import com.nbu.CSCB509.util.exception.PostExistsException;
import com.nbu.CSCB509.util.exception.PostHasIncomeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;

    private final UserService userService;

    @Autowired
    private CommentService commentService;

    private final CategoryService categoryService;

    @Autowired
    public PostService(PostRepository postRepository, UserService userService, CategoryService categoryService) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.categoryService = categoryService;
    }


    public SuccessMessage getTotalDonates(long userId) {
        Double moneyGathered = postRepository.getTotalDonatesByUserId(userId);
        if (moneyGathered == null) {
            moneyGathered = Double.valueOf(0);
        }
        return new SuccessMessage("" + moneyGathered, LocalDate.now());
    }

    public List<ShowPostNoUserDto> getPostsForUser(long userId) {
        return postRepository.findAllByUserIdOrderByDonatesDesc(userId)
                .stream()
                .map(post -> new ShowPostNoUserDto(post.getId(), post.getTitle(), post.getDescription(), post.getStartDate(), post.getEndDate(), post.getDonates(), userService.getUserById(post.getUser().getId())))
                .collect(Collectors.toList());
    }

    public List<ShowPostNoUserDto> getTop5Posts(HttpServletResponse response, HttpServletRequest request) {
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        return postRepository.findAllOrderByDonatesDescLimit5()
                .stream()
                .map(post -> new ShowPostNoUserDto(post.getId(), post.getTitle(), post.getDescription(), post.getStartDate(), post.getEndDate(), post.getDonates(), userService.getUserById(post.getUser().getId())))
                .collect(Collectors.toList());
    }

    public List<ShowPostNoUserDto> getAllPosts(HttpServletResponse response, HttpServletRequest request) {
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        return postRepository.findAllOrderByDonatesDesc()
                .stream()
                .map(post -> new ShowPostNoUserDto(post.getId(), post.getTitle(), post.getDescription(), post.getStartDate(), post.getEndDate(), post.getDonates(), userService.getUserById(post.getUser().getId())))
                .collect(Collectors.toList());
    }

    public List<ShowPostNoUserDto> getPostsPerCategory(long categoryId, HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        return postRepository.findAllByCategoryId(categoryId)
                .stream()
                .map(post -> new ShowPostNoUserDto(post.getId(), post.getTitle(), post.getDescription(), post.getStartDate(), post.getEndDate(), post.getDonates(), userService.getUserById(post.getUser().getId())))
                .collect(Collectors.toList());
    }

    public Post findPostById(long postId) {
        return postRepository.findById(postId).get();//TODO null check
    }

    public long getTotalPosts() {
        return postRepository.count();
    }

    public ShowPostNoUserDto getPostById(long postId) {
        Post post = postRepository.findById(postId).get();
        return new ShowPostNoUserDto(post.getId(), post.getTitle(), post.getDescription(), post.getStartDate(), post.getEndDate(), post.getDonates(), userService.getUserById(post.getUser().getId()));
    }

    public SuccessMessage takeDonation(long postId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        double donation = Double.parseDouble(request.getParameter("donate"));
        Post post = findPostById(postId);
        post.setDonates(donation);
        postRepository.save(post);
        response.sendRedirect("http://localhost:9999/posts.html");
        return new SuccessMessage("Donate successful", LocalDate.now());
    }


    private long countPostsByTitle(String title) {
        return postRepository.countPostByTitle(title);
    }

    public SuccessMessage deleteById(Long postId, HttpServletResponse response) throws PostHasIncomeException, IOException {
        ShowPostNoUserDto post = getPostById(postId);
        if (post.getDonates() != null) {
            throw new PostHasIncomeException();
        }
        commentService.deleteCommentsByPostId(postId);
        postRepository.deleteById(postId);
        response.sendRedirect("http://localhost:9999/profile.html");
        return new SuccessMessage("Post deleted successfully", LocalDate.now());
    }

    public ShowPostDto addPost(User user, HttpServletRequest request, HttpServletResponse response) throws ParseException, BaseException, IOException {
        long count = countPostsByTitle(request.getParameter("title"));

        if (count > 0) {
            response.sendRedirect("http://localhost:9999/postExists.html");
            throw new PostExistsException();
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String categoryName = request.getParameter("category");
        Date startDate = simpleDateFormat.parse(request.getParameter("startDate"));
        Date endDate = simpleDateFormat.parse(request.getParameter("endDate"));

        if (startDate.after(endDate)) {
            throw new BaseException("Wrong date");
        }

        Category category = categoryService.findByName(categoryName);
        Post post = new Post();
        post.setTitle(title);
        post.setDescription(description);
        post.setStartDate(startDate);
        post.setEndDate(endDate);
        post.setCategory(category);
        post.setUser(user);
        Post p = postRepository.save(post);
        response.sendRedirect("http://localhost:9999/posts.html");
        return new ShowPostDto(p.getId(), p.getTitle(), p.getDescription(), p.getStartDate(), p.getEndDate(),
                new ShowUserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getImageUrl()));
    }
}
