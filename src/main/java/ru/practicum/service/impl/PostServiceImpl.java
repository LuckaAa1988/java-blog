package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.practicum.dto.CreatePostDTO;
import ru.practicum.dto.FullPostDTO;
import ru.practicum.dto.PreviewPostDTO;
import ru.practicum.mapper.PostMapper;
import ru.practicum.repository.PostRepository;
import ru.practicum.service.PostService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    public List<PreviewPostDTO> findAllPosts() throws SQLException {
        log.info("Запрос на получение всех постов");
        return postRepository.findAllPosts().stream()
                .map(postMapper::toPreviewDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PreviewPostDTO> findAllPostsWithTag(String tag) {
        log.info("Запрос на получение всех постов с тегом: {}", tag);
        return postRepository.findAllPostsWithTag(tag).stream()
                .map(postMapper::toPreviewDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void updatePost(Long postId, String name, String text, String tags, MultipartFile image) throws IOException {
        log.info("Обновление поста с id {}", postId);
        var post = postRepository.findById(postId).get();
        post.setId(postId);
        if (name != null) {
            post.setName(name);
        }
        if (text != null) {
            post.setText(text);
        }
        if (tags != null) {
            post.setTags(Arrays.asList(tags.trim().split(",")).stream()
                    .map(String::trim)
                    .filter(tag -> !tag.isEmpty())
                    .collect(Collectors.toList()));
        }
        if (image != null) {
            post.setImage(image.getBytes());
        }
        postRepository.updatePost(post);
    }

    @Override
    public FullPostDTO findById(Long postId) {
        log.info("Получение поста с id {}", postId);
        var post = postRepository.findById(postId).get();
        var comments = postRepository.findAllCommentsByPostId(postId).reversed();
        return postMapper.toFullDTO(post, comments);
    }

    @Override
    public void createPost(CreatePostDTO postDTO) {
        log.info("Создание поста с данными: {}", postDTO);
        postRepository.createPost(postMapper.fromDTO(postDTO));
    }

    @Override
    public Integer addLike(Long postId) {
        log.info("Добалвние лайка к посту с id {}", postId);
        return postRepository.addLike(postId);
    }

    @Override
    public void deletePost(Long postId) {
        log.info("Удаление поста с id {}", postId);
        postRepository.deletePost(postId);
    }

    @Override
    public String addComment(Long postId, String text) {
        log.info("Добавление комментария к посту с id {}", postId);
        return postRepository.addComment(postId, text);
    }

    @Override
    public void updateComment(Long commentId, String text) {
        log.info("Обновление комментария с id {}", commentId);
        postRepository.updateComment(commentId, text);
    }

    @Override
    public void deleteComment(Long commentId) {
        log.info("Удаление комментария с id {}", commentId);
        postRepository.deleteComment(commentId);
    }
}
