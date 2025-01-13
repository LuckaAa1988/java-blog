package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.practicum.dto.CommentDTO;
import ru.practicum.dto.CreatePostDTO;
import ru.practicum.exception.PostNotFoundException;
import ru.practicum.service.PostService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public String blogPage(@RequestParam(value = "size", defaultValue = "10") Integer size,
                           Model model) throws SQLException {
        model.addAttribute("posts", postService.findAllPosts(size));
        model.addAttribute("createPost", CreatePostDTO.builder().build());
        return "blog";
    }

    @GetMapping("/{postId}")
    public String postPage(@PathVariable("postId") Long postId,
                           Model model) throws PostNotFoundException {
        model.addAttribute("post", postService.findById(postId));
        model.addAttribute("comment", CommentDTO.builder().build());
        model.addAttribute("createPost", CreatePostDTO.builder().build());
        return "post";
    }

    @GetMapping("/tag/{tag}")
    public String tagPage(@PathVariable("tag") String tag,
                          Model model) {
        model.addAttribute("posts", postService.findAllPostsWithTag(tag));
        model.addAttribute("createPost", CreatePostDTO.builder().build());
        return "blog";
    }

    @PostMapping
    public String addPost(@RequestParam("name") String name,
                          @RequestParam("text") String text,
                          @RequestParam("tags") String tags,
                          @RequestParam("image") MultipartFile image) throws IOException {
        postService.createPost(CreatePostDTO.builder()
                .name(name)
                .text(text)
                .tags(Arrays.asList(tags.trim().split(",")).stream()
                        .map(String::trim)
                        .filter(tag -> !tag.isEmpty())
                        .collect(Collectors.toList()))
                .image(image.getBytes())
                .build());
        return "redirect:/posts";
    }

    @PatchMapping("/{postId}")
    public String updatePost(@PathVariable("postId") Long postId,
                             @RequestParam(value = "name", required = false) String name,
                             @RequestParam(value = "text", required = false) String text,
                             @RequestParam(value = "tags", required = false) String tags,
                             @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
        postService.updatePost(postId, name, text, tags, image);
        return "redirect:/posts/" + postId;
    }

    @DeleteMapping("/{postId}")
    public String deletePost(@PathVariable("postId") Long postId) {
        postService.deletePost(postId);
        return "redirect:/posts";
    }

    @PostMapping("/{postId}/like")
    @ResponseBody
    public String addLike(@PathVariable("postId") Long postId) {
        return String.valueOf(postService.addLike(postId));
    }

    @PostMapping("/{postId}/comment")
    public String addComment(@PathVariable("postId") Long postId,
                             @RequestParam(value = "text") String text) {
        postService.addComment(postId, text);
        return "redirect:/posts/" + postId;
    }

    @PatchMapping("/{postId}/comment/{commentId}")
    public String updateComment(@PathVariable("postId") Long postId,
                                @PathVariable("commentId") Long commentId,
                                @RequestParam(value = "text") String text) {
        postService.updateComment(commentId, text);
        return "redirect:/posts/" + postId;
    }

    @DeleteMapping("/{postId}/comment/{commentId}")
    public String deletePost(@PathVariable("postId") Long postId,
                             @PathVariable("commentId") Long commentId) {
        postService.deleteComment(commentId);
        return "redirect:/posts/" + postId;
    }

}
