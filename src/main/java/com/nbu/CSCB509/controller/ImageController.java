package com.nbu.CSCB509.controller;

import com.nbu.CSCB509.model.entity.User;
import com.nbu.CSCB509.service.UserService;
import com.nbu.CSCB509.util.SuccessMessage;
import com.nbu.CSCB509.util.exception.ImageMissingException;
import com.nbu.CSCB509.util.exception.NotLoggedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

@RestController
@RequestMapping(value = "/images")
public class ImageController extends BaseController {
    private static final String IMAGE_PATH = "C:\\Users\\Ivelina\\OneDrive\\Работен плот\\images\\";

    private final UserService userService;

    @Autowired
    public ImageController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public SuccessMessage userImageUpload(@RequestPart(value = "image") MultipartFile file, HttpSession session, HttpServletResponse response) throws SQLException, NotLoggedException, IOException {
        validateLogin(session, response);
        User user = (User) session.getAttribute("user");
        String name = user.getId() + System.currentTimeMillis() + ".png";
        File newImage = new File(IMAGE_PATH + name);
        file.transferTo(newImage);
        userService.addImageUrl(IMAGE_PATH, name, user.getId());
        user.setImageUrl(name);
        response.sendRedirect("http://localhost:9999/profile.html");
        return new SuccessMessage("Image uploaded", LocalDate.now());
    }

    @PostMapping(value = "/posts/{id}")
    public SuccessMessage postImageUpload(@PathVariable("id") Long postId, @RequestPart(value = "image") MultipartFile file) throws IOException {
        String name = postId + System.currentTimeMillis() + ".png";
        File newImage = new File(IMAGE_PATH + name);
        file.transferTo(newImage);
        userService.addImageUrl(IMAGE_PATH, name, postId);
        return new SuccessMessage("Image uploaded", LocalDate.now());
    }

    @GetMapping(value = "/users/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] downloadImageById(@PathVariable("id") long userId) throws Exception {
        String imageUrl = this.userService.getImageUrl(userId);
        if (imageUrl == null) {
            throw new ImageMissingException();
        }
        File image = new File(IMAGE_PATH + imageUrl);
        FileInputStream fis = new FileInputStream(image);
        return fis.readAllBytes();
    }

    @GetMapping(value = "/posts/{url}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] downloadImageByName(@PathVariable("url") String imageUrl) throws Exception {
        if (imageUrl == null) {
            throw new ImageMissingException();
        }
        File image = new File(IMAGE_PATH + imageUrl);
        FileInputStream fis = new FileInputStream(image);
        return fis.readAllBytes();
    }

    @DeleteMapping
    public SuccessMessage deleteImage(HttpSession session, HttpServletResponse response) throws ImageMissingException, NotLoggedException, IOException {
        validateLogin(session, response);
        User user = (User) session.getAttribute("user");
        userService.deleteImage(IMAGE_PATH, user);
        return new SuccessMessage("Image deleted", LocalDate.now());
    }

}
