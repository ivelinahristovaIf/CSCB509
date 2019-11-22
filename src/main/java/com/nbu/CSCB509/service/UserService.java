package com.nbu.CSCB509.service;

import com.nbu.CSCB509.model.dto.ShowUserDto;
import com.nbu.CSCB509.model.entity.User;
import com.nbu.CSCB509.repository.UserRepository;
import com.nbu.CSCB509.util.PasswordEncoder;
import com.nbu.CSCB509.util.SuccessMessage;
import com.nbu.CSCB509.util.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public SuccessMessage register(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws BaseException, IOException {
        String email = request.getParameter("email");
        checkEmail(email);
        String password = request.getParameter("password");
        String password2 = request.getParameter("password2");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        if (email.isEmpty() || password.isEmpty() || password2.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            throw new MissingValuableFieldsException();
        }
        int count = this.userRepository.countUserByEmail(email);
        if (count > 0) {
            response.sendRedirect("http://localhost:9999/register.html");
            throw new UserExistsException();
        }
        if (!password.equals(password2)) {
            throw new PasswordsNotMatchingException();
        }
        User user = new User();
        user.setLastName(lastName);
        user.setFirstName(firstName);
        user.setEmail(email);
        user.setPassword(PasswordEncoder.hashPassword(password));
        userRepository.save(user);
        request.getSession().setAttribute("user", user);
        session.setMaxInactiveInterval((60 * 60));
        response.sendRedirect("http://localhost:9999/profile.html");
        return new SuccessMessage("Register successful", LocalDate.now());
    }

    public ShowUserDto login(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws BaseException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        if (email.isEmpty() || password.isEmpty()) {
            throw new MissingValuableFieldsException();
        }

        int count = userRepository.countUserByEmail(email);
        User user = userRepository.findByEmail(email);

        if (count < 1 || !BCrypt.checkpw(password, user.getPassword())) {
            response.sendRedirect("http://localhost:9999/login.html");
            throw new WrongCredentialsException();
        }

        session.setMaxInactiveInterval(60 * 60);
        session.setAttribute("user", user);
        response.sendRedirect("http://localhost:9999/profile.html");
        return new ShowUserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getImageUrl());
    }

    private void checkEmail(String email) throws EmailInvalidFormatException {
        String emailRegex = "([A-Za-z0-9-_.]+@[A-Za-z0-9-_]+(?:\\.[A-Za-z]+)+)";
        if (!email.matches(emailRegex)) {
            throw new EmailInvalidFormatException();
        }
    }

    public SuccessMessage logout(HttpSession session, HttpServletResponse response) throws IOException {
        session.invalidate();
        response.sendRedirect("http://localhost:9999");
        return new SuccessMessage("You logged out", LocalDate.now());
    }

    public ShowUserDto getUser(HttpSession session){
        User logged = (User) session.getAttribute("user");
        return getUserById(logged.getId());
    }

    User findUserById(long userId) {
        return userRepository.findById(userId).get();
    }

    ShowUserDto getUserById(long id) {
        User user = userRepository.findById(id).get();
        return new ShowUserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getImageUrl());
    }

    public void addImageUrl(String dir, String imageUrl, long userId) {
        User user = userRepository.findById(userId).get();
        File file = new File(dir + user.getImageUrl());
        file.delete();

        user.setImageUrl(imageUrl);
        userRepository.save(user);
    }

    public String getImageUrl(long userId) {
        return userRepository.findById(userId).get().getImageUrl();
    }

    public void deleteImage(String dir, User user) throws ImageMissingException {
        File file = new File(dir + user.getImageUrl());
        if (!file.delete()) {
            throw new ImageMissingException();
        }
        user.setImageUrl(null);
        userRepository.save(user);
    }

    public Long getCount(){
        return userRepository.count();
    }

}
