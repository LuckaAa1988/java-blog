package ru.practicum.repository;

import ru.practicum.model.Comment;
import ru.practicum.model.Post;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface PostRepository {
    List<Post> findAllPosts(Integer size) throws SQLException;
    Optional<Post> findById(Long postId);
    boolean existById(Long postId);
    List<Comment> findAllCommentsByPostId(Long postId);

    void createPost(Post post);

    Integer addLike(Long postId);

    void deletePost(Long postId);

    String addComment(Long postId, String text);

    void updateComment(Long commentId, String text);

    void deleteComment(Long commentId);

    List<Post> findAllPostsWithTag(String tag);

    void updatePost(Post post);
}
