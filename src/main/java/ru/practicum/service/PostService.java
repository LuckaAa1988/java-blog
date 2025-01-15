package ru.practicum.service;

import org.springframework.web.multipart.MultipartFile;
import ru.practicum.dto.CreatePostDTO;
import ru.practicum.dto.FullPostDTO;
import ru.practicum.dto.PreviewPostDTO;
import ru.practicum.exception.PostNotFoundException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface PostService {
    List<PreviewPostDTO> findAllPosts(Integer size) throws SQLException;

    FullPostDTO findById(Long postId) throws PostNotFoundException;

    void createPost(CreatePostDTO postDTO) throws IOException;

    Integer addLike(Long postId);

    void deletePost(Long postId);

    String addComment(Long postId, String text);

    void updateComment(Long commentId, String text);

    void deleteComment(Long commentId);

    List<PreviewPostDTO> findAllPostsWithTag(String tag);

    void updatePost(Long postId, String name, String text, String tags, MultipartFile image) throws IOException;
}
