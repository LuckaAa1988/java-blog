package ru.practicum.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Post;

import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<Post> findAllPosts() throws SQLException {
        return jdbcTemplate.query("SELECT id, name, text FROM posts", (rs, rowNum) -> Post.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .text(rs.getString("text"))
                .build()
        );
    }
}
