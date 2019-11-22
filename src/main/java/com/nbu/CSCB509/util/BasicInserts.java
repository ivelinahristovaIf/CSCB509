package com.nbu.CSCB509.util;

import com.nbu.CSCB509.model.entity.Category;
import com.nbu.CSCB509.model.entity.Comment;
import com.nbu.CSCB509.model.entity.Post;
import com.nbu.CSCB509.model.entity.User;
import com.nbu.CSCB509.repository.CategoryRepository;
import com.nbu.CSCB509.repository.CommentRepository;
import com.nbu.CSCB509.repository.PostRepository;
import com.nbu.CSCB509.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Date;
import java.time.LocalDate;

@Component
public class BasicInserts {

    public static final String IVELINA_EMAIL = "ivelina@abv.bg";
    public static final String SASHO_EMAIL = "sasho@abv.bg";
    public static final String FIRST_POST_TITLE = "First post";
    public static final String SECOND_POST_TITLE = "Second post";
    public static final String TRAVEL_NAME = "Travel";
    public static final String SCIENCE_NAME = "Science";
    public static final String SPORTS_NAME = "Sports";
    public static final String TECHNOLOGY_NAME = "Technology";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;

    @PostConstruct
    public void appReady(){
        User user = userRepository.findByEmail(IVELINA_EMAIL);
        if (user == null) {
            user = new User();
            user.setFirstName("Ivelina");
            user.setLastName("Hristova");
            user.setEmail(IVELINA_EMAIL);
            user.setPassword(PasswordEncoder.hashPassword("12345678"));
            userRepository.save(user);
        }

        User user1 = userRepository.findByEmail(SASHO_EMAIL);
        if (user1 == null) {
            user1 = new User();
            user1.setFirstName("Sasho");
            user1.setLastName("Minchev");
            user1.setEmail(SASHO_EMAIL);
            user1.setPassword(PasswordEncoder.hashPassword("12345678"));
            userRepository.save(user1);
        }

        if (categoryRepository.count() == 0) {
            Category travel = new Category();
            travel.setName(TRAVEL_NAME);
            categoryRepository.save(travel);

            Category science = new Category();
            science.setName(SCIENCE_NAME);
            categoryRepository.save(science);

            Category sport = new Category();
            sport.setName(SPORTS_NAME);
            categoryRepository.save(sport);

            Category technology = new Category();
            technology.setName(TECHNOLOGY_NAME);
            categoryRepository.save(technology);
        }


        Post post = postRepository.findByTitle(FIRST_POST_TITLE);
        if (post == null) {
            post = new Post();
            post.setTitle(FIRST_POST_TITLE);
            post.setDescription("This is the first post ever posted on idea starter by the admin!");
            post.setStartDate(Date.valueOf(LocalDate.now()));
            post.setEndDate(Date.valueOf(LocalDate.now().plusWeeks(1)));
            post.setCategory(categoryRepository.findByName(TRAVEL_NAME));
            post.setUser(user);
            postRepository.save(post);
        }

        Post post1 = postRepository.findByTitle(SECOND_POST_TITLE);
        if (post1 == null) {
            post1 = new Post();
            post1.setTitle(SECOND_POST_TITLE);
            post1.setDescription("This is the second post on idea starter by sasho!");
            post1.setStartDate(Date.valueOf(LocalDate.now()));
            post1.setEndDate(Date.valueOf(LocalDate.now().plusWeeks(1)));
            post1.setCategory(categoryRepository.findByName(SCIENCE_NAME));
            post1.setUser(user1);
            postRepository.save(post1);
        }

        if (commentRepository.count() == 0) {
            Comment comment = new Comment();
            comment.setComment("First ever comment by sasho");
            comment.setPost(postRepository.findByTitle(FIRST_POST_TITLE));
            comment.setUser(userRepository.findByEmail(SASHO_EMAIL));
            commentRepository.save(comment);


            Comment comment1 = new Comment();
            comment1.setComment("First ever comment by admin");
            comment1.setPost(postRepository.findByTitle(SECOND_POST_TITLE));
            comment1.setUser(userRepository.findByEmail(IVELINA_EMAIL));
            commentRepository.save(comment1);


            Comment comment2 = new Comment();
            comment2.setComment("Second comment by sasho");
            comment2.setPost(postRepository.findByTitle(SECOND_POST_TITLE));
            comment2.setUser(userRepository.findByEmail(SASHO_EMAIL));
            commentRepository.save(comment2);
        }
    }

}
