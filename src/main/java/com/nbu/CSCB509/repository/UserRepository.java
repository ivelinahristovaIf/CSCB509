package com.nbu.CSCB509.repository;

import com.nbu.CSCB509.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    int countUserByEmail(String email);

    String findImageUrlById(long id);

}
