package ru.practicum.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.configuration.TestConfig;
import ru.practicum.dto.CreatePostDTO;
import ru.practicum.exception.PostNotFoundException;
import ru.practicum.service.impl.PostServiceImpl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
public class IntegrationTest {

    @Autowired
    private PostServiceImpl postService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    void resetDatabase() {
        jdbcTemplate.execute("DROP ALL OBJECTS");
        jdbcTemplate.execute("RUNSCRIPT FROM 'classpath:schema.sql'");
    }


    @Test
    public void createPostTest() throws PostNotFoundException {
        var post = CreatePostDTO.builder()
                .name("Пост 1")
                .tags(List.of())
                .text("Текст для поста 1")
                .image(new byte[]{})
                .build();
        postService.createPost(post);

        var savedPost = postService.findById(1L);

        assertAll(
                () -> assertNotNull(savedPost),
                () -> assertEquals(post.getName(), savedPost.getName()),
                () -> assertEquals(post.getText(), savedPost.getText())
        );


    }

    @Test
    public void findAllPostsTest() throws SQLException {
        var post = CreatePostDTO.builder()
                .name("Пост 1")
                .tags(List.of())
                .text("Текст для поста 1")
                .image(new byte[]{})
                .build();

        var post2 = CreatePostDTO.builder()
                .name("Пост 2")
                .tags(List.of())
                .text("Текст для поста 2")
                .image(new byte[]{})
                .build();

        postService.createPost(post);
        postService.createPost(post2);

        var savedPosts = postService.findAllPosts(10);

        assertAll(
                () -> assertNotNull(savedPosts),
                () -> assertEquals(2, savedPosts.size())
        );
    }

    @Test
    void findAllPostsWithTagTest() {
        var post = CreatePostDTO.builder()
                .name("Пост 1")
                .tags(List.of("Тэг 1"))
                .text("Текст для поста 1")
                .image(new byte[]{})
                .build();

        var post2 = CreatePostDTO.builder()
                .name("Пост 2")
                .tags(List.of("Тэг 2"))
                .text("Текст для поста 2")
                .image(new byte[]{})
                .build();

        postService.createPost(post);
        postService.createPost(post2);

        var savedPosts = postService.findAllPostsWithTag("Тэг 1");

        assertAll(
                () -> assertNotNull(savedPosts),
                () -> assertEquals(1, savedPosts.size())
        );
    }

    @Test
    void updatePostTest() throws IOException, PostNotFoundException {
        var post = CreatePostDTO.builder()
                .name("Пост 1")
                .tags(List.of("Тэг 1"))
                .text("Текст для поста 1")
                .image(new byte[]{})
                .build();

        postService.createPost(post);

        postService.updatePost(1L,
                "Обновленный пост",
                "Обновленный текст",
                "Тэг",
                null);

        var updatedPost = postService.findById(1L);

        assertAll(
                () -> assertNotNull(post),
                () -> assertEquals(1L, updatedPost.getId()),
                () -> assertEquals("Обновленный пост", updatedPost.getName()),
                () -> assertEquals("Обновленный текст", updatedPost.getText()),
                () -> assertEquals("Тэг", updatedPost.getTags().get(0))
        );

    }

    @Test
    void addLikeTest() throws PostNotFoundException {
        var post = CreatePostDTO.builder()
                .name("Пост 1")
                .tags(List.of())
                .text("Текст для поста 1")
                .image(new byte[]{})
                .build();

        postService.createPost(post);

        var beforeLikePost = postService.findById(1L);

        assertEquals(0, beforeLikePost.getLikes());

        postService.addLike(1L);
        postService.addLike(1L);

        var afterLikePost = postService.findById(1L);

        assertEquals(2, afterLikePost.getLikes());

    }

    @Test
    void deletePostTest() throws PostNotFoundException {
        var post = CreatePostDTO.builder()
                .name("Пост 1")
                .tags(List.of())
                .text("Текст для поста 1")
                .image(new byte[]{})
                .build();

        postService.createPost(post);

        var savedPost = postService.findById(1L);

        assertNotNull(savedPost);

        postService.deletePost(1L);

        assertThrows(PostNotFoundException.class, () -> postService.findById(1L));
    }

    @Test
    void addCommentTest() throws PostNotFoundException {
        var post = CreatePostDTO.builder()
                .name("Пост 1")
                .tags(List.of())
                .text("Текст для поста 1")
                .image(new byte[]{})
                .build();

        postService.createPost(post);

        postService.addComment(1L, "Текст комментария");

        var savedPost = postService.findById(1L);

        assertAll(
                () -> assertEquals(1, savedPost.getComments().size()),
                () -> assertEquals("Текст комментария", savedPost.getComments().get(0).getText())
        );
    }

    @Test
    void updateCommentTest() throws PostNotFoundException {
        var post = CreatePostDTO.builder()
                .name("Пост 1")
                .tags(List.of())
                .text("Текст для поста 1")
                .image(new byte[]{})
                .build();

        postService.createPost(post);

        postService.addComment(1L, "Текст комментария");

        var savedPost = postService.findById(1L);

        assertAll(
                () -> assertEquals(1, savedPost.getComments().size()),
                () -> assertEquals("Текст комментария", savedPost.getComments().get(0).getText())
        );

        postService.updateComment(1L, "Обновленный комментарий");

        var savedUpdatedPost = postService.findById(1L);

        assertAll(
                () -> assertEquals(1, savedUpdatedPost.getComments().size()),
                () -> assertEquals("Обновленный комментарий", savedUpdatedPost.getComments().get(0).getText())
        );
    }

    @Test
    void deleteCommentTest() throws PostNotFoundException {
        var post = CreatePostDTO.builder()
                .name("Пост 1")
                .tags(List.of())
                .text("Текст для поста 1")
                .image(new byte[]{})
                .build();

        postService.createPost(post);

        postService.addComment(1L, "Текст комментария");

        var savedPost = postService.findById(1L);

        assertAll(
                () -> assertEquals(1, savedPost.getComments().size()),
                () -> assertEquals("Текст комментария", savedPost.getComments().get(0).getText())
        );

        postService.deleteComment(1L);

        var savedUpdatedPost = postService.findById(1L);

        assertAll(
                () -> assertEquals(0, savedUpdatedPost.getComments().size())
        );
    }

}
