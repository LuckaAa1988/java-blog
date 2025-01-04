package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CreatePostDTO;
import ru.practicum.dto.UpdatePostDTO;

@Controller
@RequestMapping("/blog/posts")
@RequiredArgsConstructor
public class PostController {

    @GetMapping("/{postId}")
    public String postPage(@PathVariable Long postId,
                           Model model) {
        return "post";
    }

    @PostMapping
    public String addPost(CreatePostDTO postDTO) {
        return "post";
    }

    @PatchMapping("/{postId}")
    public String updatePost(@PathVariable Long postId,
                             UpdatePostDTO postDTO) {
        return "post";
    }

    @DeleteMapping("/{postId}")
    public String deletePost(@PathVariable Long postId) {
        return "post";
    }

}
