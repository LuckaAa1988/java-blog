package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.PostService;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class CommentController {

    private final PostService postService;

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
    public String deleteComment(@PathVariable("postId") Long postId,
                             @PathVariable("commentId") Long commentId) {
        postService.deleteComment(commentId);
        return "redirect:/posts/" + postId;
    }
}
