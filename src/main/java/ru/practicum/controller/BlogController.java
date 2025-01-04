package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.practicum.service.PostService;

import java.sql.SQLException;

@Controller
@RequestMapping("/blog")
@RequiredArgsConstructor
public class BlogController {

    private final PostService postService;

    @GetMapping("/")
    public String blogPage(Model model) throws SQLException {
        model.addAttribute("posts", postService.findAllPosts());
        return "blog";
    }
}
