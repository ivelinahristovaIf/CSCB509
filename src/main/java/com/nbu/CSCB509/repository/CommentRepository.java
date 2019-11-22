package com.nbu.CSCB509.repository;

import com.nbu.CSCB509.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Modifying
    @Query(value = "DELETE FROM comments WHERE post_id = :postId", nativeQuery = true)
    void deleteAllByPostId(@Param("postId") Long postId);

    @Query(value = "SELECT COUNT(DISTINCT (user_id)) AS count from comments WHERE post_id = :postId", nativeQuery = true)
    Integer getDistinctCommentsByPostId(@Param("postId") long postId);

    List<Comment> findAllByPostId(long postId);
}
