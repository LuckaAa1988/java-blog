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
import ru.practicum.controller.CommentController;
import ru.practicum.service.impl.PostServiceImpl;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebAppConfiguration
public class CommentControllerTest {

    private MockMvc mockMvc;
    @Mock
    private PostServiceImpl postService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CommentController(postService)).build();
    }

    @Test
    void shouldAddComment() throws Exception {
        mockMvc.perform(post("/posts/1/comment")
                        .param("text", "Коммент"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));

        Mockito.verify(postService).addComment(1L, "Коммент");
    }

    @Test
    void shouldUpdateComment() throws Exception {
        mockMvc.perform(multipart("/posts/1/comment/2")
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        })
                        .param("text", "Обновленный коммент"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));

        Mockito.verify(postService).updateComment(2L, "Обновленный коммент");
    }

    @Test
    void shouldDeleteComment() throws Exception {
        mockMvc.perform(delete("/posts/1/comment/2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));

        Mockito.verify(postService).deleteComment(2L);
    }
}
