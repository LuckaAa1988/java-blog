package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.model.Post;
import ru.practicum.repository.PostRepository;

import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public List<Post> findAllPosts() throws SQLException {
        return postRepository.findAllPosts();
    }
}
