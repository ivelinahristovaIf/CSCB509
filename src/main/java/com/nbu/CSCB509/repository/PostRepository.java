package com.nbu.CSCB509.repository;

import com.nbu.CSCB509.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "SELECT COUNT(*) FROM posts WHERE title LIKE :title", nativeQuery = true)
    long countPostByTitle(@Param("title") String title);

    Post findByTitle(String title);

    @Query(value = "SELECT SUM(donates) AS maxDonates from posts WHERE user_id = :userId", nativeQuery = true)
    Double getTotalDonatesByUserId(@Param("userId") long userId);

    @Query(value = "SELECT * FROM posts WHERE user_id = :userId ORDER BY donates DESC", nativeQuery = true)
    List<Post> findAllByUserIdOrderByDonatesDesc(@Param("userId") long userId);

    @Query(value = "SELECT * FROM posts ORDER BY donates DESC LIMIT 5", nativeQuery = true)
    List<Post> findAllOrderByDonatesDescLimit5();

    @Query(value = "SELECT * FROM posts ORDER BY donates DESC", nativeQuery = true)
    List<Post> findAllOrderByDonatesDesc();

    List<Post> findAllByCategoryId(long categoryId);

    @Modifying
    @Query(value = "DELETE FROM posts WHERE id = :postId", nativeQuery = true)
    void deleteById(@Param("postId") Long postId);

}
