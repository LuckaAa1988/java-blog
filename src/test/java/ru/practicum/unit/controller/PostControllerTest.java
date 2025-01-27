package ru.practicum.unit.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.controller.PostController;
import ru.practicum.dto.FullPostDTO;
import ru.practicum.service.impl.PostServiceImpl;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebAppConfiguration
public class PostControllerTest {

    private MockMvc mockMvc;
    @Mock
    private PostServiceImpl postService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new PostController(postService)).build();
    }

    @Test
    void shouldReturnHtmlWithPosts() throws Exception {
        Mockito.when(postService.findAllPosts(10)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(view().name("blog"))
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attributeExists("createPost"));
    }

    @Test
    void shouldReturnPostPage() throws Exception {
        FullPostDTO post = FullPostDTO.builder().build();
        Mockito.when(postService.findById(1L)).thenReturn(post);

        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("post"))
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attributeExists("comment"))
                .andExpect(model().attributeExists("createPost"));
    }

    @Test
    void shouldReturnPostsWithTag() throws Exception {
        Mockito.when(postService.findAllPostsWithTag("tag")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/posts/tag/tag"))
                .andExpect(status().isOk())
                .andExpect(view().name("blog"))
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attributeExists("createPost"));
    }

    @Test
    void shouldAddPost() throws Exception {
        byte[] imageBytes = new byte[]{};
        mockMvc.perform(multipart("/posts")
                        .file("image", imageBytes)
                        .param("name", "Пост 1")
                        .param("text", "Пост текст")
                        .param("tags", "тэг1, тэг2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));

        Mockito.verify(postService).createPost(Mockito.argThat(dto -> {
            try {
                return "Пост 1".equals(dto.getName())
                        && "Пост текст".equals(dto.getText())
                        && List.of("тэг1", "тэг2").equals(dto.getTags())
                        && Arrays.equals(imageBytes, dto.getImage().getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    @Test
    void shouldUpdatePost() throws Exception {
        mockMvc.perform(multipart("/posts/1")
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        })
                        .param("name", "Обновленное имя")
                        .param("text", "Обновленный текст")
                        .param("tags", "тэг1, тэг2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));

        Mockito.verify(postService).updatePost(1L, "Обновленное имя", "Обновленный текст", "тэг1, тэг2", null);
    }

    @Test
    void shouldDeletePost() throws Exception {
        mockMvc.perform(delete("/posts/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));

        Mockito.verify(postService).deletePost(1L);
    }

    @Test
    void shouldAddLike() throws Exception {
        Mockito.when(postService.addLike(1L)).thenReturn(5);

        mockMvc.perform(post("/posts/1/like"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));

        Mockito.verify(postService).addLike(1L);
    }
}
