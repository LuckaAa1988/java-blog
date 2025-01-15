package ru.practicum.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import ru.practicum.dto.CreatePostDTO;
import ru.practicum.dto.FullPostDTO;
import ru.practicum.dto.PreviewPostDTO;
import ru.practicum.exception.PostNotFoundException;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.mapper.PostMapper;
import ru.practicum.model.Post;
import ru.practicum.repository.PostRepository;
import ru.practicum.service.impl.PostServiceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;
    @Mock
    private PostMapper postMapper;
    @Mock
    private CommentMapper commentMapper;
    @InjectMocks
    private PostServiceImpl postService;
    private Post post1;
    private Post post2;
    private Post post3;
    private List<Post> posts;

    @BeforeEach
    void setUp() {
        post1 = Post.builder()
                .id(1L)
                .name("Пост 1")
                .text("Текст для поста 1")
                .tags(List.of("тег1", "тег2"))
                .likes(3)
                .image(" ")
                .commentsCount(2)
                .build();
        post2 = Post.builder()
                .id(2L)
                .name("Пост 2")
                .text("Текст для поста 2")
                .tags(List.of("тег1"))
                .likes(1)
                .image(" ")
                .commentsCount(1)
                .build();
        post3 = Post.builder()
                .id(3L)
                .name("Пост 3")
                .text("Текст для поста 3")
                .tags(List.of("тег2"))
                .likes(0)
                .image(" ")
                .commentsCount(0)
                .build();
        posts = List.of(post1, post2, post3);
    }

    @Test
    void findAllPostsTest() throws SQLException {
        when(postRepository.findAllPosts(10)).thenReturn(posts);
        when(postMapper.toPreviewDTO(post1)).thenReturn(PreviewPostDTO.builder()
                .id(1L)
                .name("Пост 1")
                .shortText("Текст для поста 1")
                .tags(List.of("тег1", "тег2"))
                .likes(3)
                .commentsCount(2)
                .build());
        when(postMapper.toPreviewDTO(post2)).thenReturn(PreviewPostDTO.builder()
                .id(2L)
                .name("Пост 2")
                .shortText("Текст для поста 2")
                .tags(List.of("тег1"))
                .likes(1)
                .commentsCount(1)
                .build());
        when(postMapper.toPreviewDTO(post3)).thenReturn(PreviewPostDTO.builder()
                .id(3L)
                .name("Пост 3")
                .shortText("Текст для поста 3")
                .tags(List.of("тег2"))
                .likes(0)
                .commentsCount(0)
                .build());

        var result = postService.findAllPosts(10);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(3, result.size()),
                () -> assertEquals("Пост 1", result.get(0).getName()),
                () -> assertEquals("Пост 2", result.get(1).getName()),
                () -> assertEquals("Пост 3", result.get(2).getName())
        );
    }

    @Test
    void findAllPostsWithTagTest() {
        when(postRepository.findAllPostsWithTag("тег1")).thenReturn(List.of(post1, post2));
        when(postMapper.toPreviewDTO(post1)).thenReturn(PreviewPostDTO.builder()
                .id(1L)
                .name("Пост 1")
                .shortText("Текст для поста 1")
                .tags(List.of("тег1", "тег2"))
                .likes(3)
                .commentsCount(2)
                .build());
        when(postMapper.toPreviewDTO(post2)).thenReturn(PreviewPostDTO.builder()
                .id(2L)
                .name("Пост 2")
                .shortText("Текст для поста 2")
                .tags(List.of("тег1"))
                .likes(1)
                .commentsCount(1)
                .build());

        var result = postService.findAllPostsWithTag("тег1");

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size()),
                () -> assertEquals("Пост 1", result.get(0).getName()),
                () -> assertEquals("Пост 2", result.get(1).getName())
        );
    }

    @Test
    void updatePostTest() throws IOException {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post1));

        postService.updatePost(1L, "Обновленный пост", "Обновленный текст", "тег3", new MockMultipartFile(" ", new byte[]{}));

        assertAll(
                () -> assertEquals("Обновленный пост", post1.getName()),
                () -> assertEquals("Обновленный текст", post1.getText()),
                () -> assertEquals(List.of("тег3"), post1.getTags())
        );
    }

    @Test
    void findByIdTest() throws PostNotFoundException {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post1));
        when(postRepository.findAllCommentsByPostId(1L)).thenReturn(List.of());
        when(postMapper.toFullDTO(post1, List.of())).thenReturn(FullPostDTO.builder()
                .id(1L)
                .name("Пост 1")
                .text("Текст для поста 1")
                .tags(List.of("тег1", "тег2"))
                .comments(List.of())
                .likes(3)
                .build());

        var result = postService.findById(1L);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("Пост 1", result.getName()),
                () -> assertEquals(3, result.getLikes())
        );
    }

    @Test
    void createPostTest() throws IOException {
        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);

        postService.createPost(CreatePostDTO.builder()
                .name("Пост 1")
                .text("Текст для поста 1")
                .tags(List.of("тег1", "тег2"))
                .image(new MockMultipartFile(" ", new byte[]{}))
                .build());


        verify(postRepository).createPost(postCaptor.capture());

        Post capturedPost = postCaptor.getValue();
        assertEquals("Пост 1", capturedPost.getName());
        assertEquals("Текст для поста 1", capturedPost.getText());
        assertEquals(List.of("тег1", "тег2"), capturedPost.getTags());
        assertTrue(capturedPost.getImage() instanceof String);
    }

    @Test
    void addLikeTest() {
        when(postRepository.addLike(1L)).thenReturn(4);

        var result = postService.addLike(1L);

        assertEquals(4, result);
    }

    @Test
    void deletePostTest() {
        postService.deletePost(1L);

        verify(postRepository).deletePost(1L);
    }

    @Test
    void addCommentTest() {
        when(postRepository.addComment(1L, "Новый комментарий")).thenReturn("Новый комментарий");

        var result = postService.addComment(1L, "Новый комментарий");

        assertEquals("Новый комментарий", result);
    }

    @Test
    void updateCommentTest() {
        postService.updateComment(1L, "Обновленный комментарий");

        verify(postRepository).updateComment(1L, "Обновленный комментарий");
    }

    @Test
    void deleteCommentTest() {
        postService.deleteComment(1L);

        verify(postRepository).deleteComment(1L);
    }
}
