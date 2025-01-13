package ru.practicum.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.Comment;
import ru.practicum.model.Post;
import ru.practicum.repository.PostRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {

    private final JdbcTemplate jdbcTemplate;
    private final String FIND_ALL_POSTS_SQL = """
            SELECT p.id, p.name, p.image, p.text, p.likes, t.tag, COUNT(c.id) AS count
            FROM posts AS p
                     LEFT JOIN comments c on p.id = c.post_id
                     LEFT JOIN tags_posts tp ON p.id = tp.post_id
                     LEFT JOIN tags t ON tp.tag_id = t.id
            GROUP BY p.id, p.name, p.image, p.text, p.likes, t.tag
            """;
    private final String FIND_ALL_POSTS_WITH_TAG_SQL = """
            SELECT p.id, p.name, p.image, p.text, p.likes, t.tag, COUNT(c.id) AS count
            FROM posts AS p
                     LEFT JOIN comments c on p.id = c.post_id
                     LEFT JOIN tags_posts tp ON p.id = tp.post_id
                     LEFT JOIN tags t ON tp.tag_id = t.id
                     WHERE t.tag = ?
            GROUP BY p.id, p.name, p.image, p.text, p.likes, t.tag
            """;
    private final String FIND_POST_BY_ID_SQL = """
            SELECT p.id, p.name, p.image, p.text, p.likes, t.tag
            FROM posts AS p
            LEFT JOIN tags_posts tp ON p.id = tp.post_id
            LEFT JOIN tags t ON tp.tag_id = t.id
            WHERE p.id = ?
            """;
    private final String CREATE_POST_SQL = """
            INSERT INTO posts (name, text, image) VALUES (?, ?, ?);
            """;
    private final String CREATE_TAG_SQL = """
            INSERT INTO tags (tag) VALUES (?);
            """;
    private final String TAGS_POST_SQL = """
            INSERT INTO tags_posts (post_id, tag_id) VALUES (?, ?);
            """;
    private final String FIND_TAG_BY_POST_ID_SQL = """
            SELECT t.tag FROM tags t
            JOIN tags_posts tp ON t.id = tp.tag_id
            WHERE tp.post_id = ?
            """;
    private final String DELETE_TAGS_SQL = """
            DELETE FROM tags_posts WHERE post_id = ?
            """;
    private final String FIND_TAG_SQL = """
            SELECT id FROM tags WHERE tag = ?
            """;
    private final String ADD_LIKE_SQL = """
            UPDATE posts SET likes = likes + 1 WHERE id = ?
            """;
    private final String EXIST_POST_SQL = """
            SELECT EXISTS (SELECT 1 FROM posts WHERE id = ?)
            """;
    private final String DELETE_POST_SQL = """
            DELETE FROM posts WHERE id = ?
            """;
    private final String DELETE_COMMENT_SQL = """
            DELETE FROM comments WHERE id = ?
            """;
    private final String ADD_COMMENT_SQL = """
            INSERT INTO comments (text, post_id) VALUES (?, ?);
            """;
    private final String UPDATE_COMMENT_SQL = """
            UPDATE comments SET text = ? WHERE id = ?
            """;
    private final String UPDATE_POST_SQL = """
            UPDATE posts SET name = ?, text = ?, image = ? WHERE id = ?
            """;
    private final String FIND_ALL_COMMENTS = """
            SELECT id, text FROM comments WHERE post_id = ?
            """;

    @Override
    public List<Post> findAllPosts(Integer size) {
        Map<Long, Post> postMap = new HashMap<>();

        jdbcTemplate.query(FIND_ALL_POSTS_SQL, rs -> {
            Long postId = rs.getLong("id");

            Post post = postMap.get(postId);
            if (post == null) {
                post = Post.builder()
                        .id(postId)
                        .name(rs.getString("name"))
                        .image(rs.getBytes("image"))
                        .text(rs.getString("text"))
                        .likes(rs.getInt("likes"))
                        .commentsCount(rs.getInt("count"))
                        .tags(new ArrayList<>())
                        .build();
                postMap.put(postId, post);
            }

            String tag = rs.getString("tag");
            if (tag != null && !post.getTags().contains(tag)) {
                post.getTags().add(tag);
            }
        });
        if (size > postMap.values().size()) {
            size = postMap.values().size();
        }
        return new ArrayList<>(postMap.values()).reversed().subList(0, size);
    }

    @Override
    @Transactional
    public List<Post> findAllPostsWithTag(String tag) {
        Map<Long, Post> postMap = new HashMap<>();

        jdbcTemplate.query(FIND_ALL_POSTS_WITH_TAG_SQL, rs -> {
            Long postId = rs.getLong("id");

            Post post = postMap.get(postId);
            if (post == null) {
                post = Post.builder()
                        .id(postId)
                        .name(rs.getString("name"))
                        .image(rs.getBytes("image"))
                        .text(rs.getString("text"))
                        .likes(rs.getInt("likes"))
                        .commentsCount(rs.getInt("count"))
                        .tags(new ArrayList<>())
                        .build();
                postMap.put(postId, post);
            }

            String innerTag = rs.getString("tag");
            if (innerTag != null && !post.getTags().contains(innerTag)) {
                post.getTags().add(innerTag);
            }
        }, tag);

        for (Post post : postMap.values()) {
            List<String> allTags = jdbcTemplate.query(FIND_TAG_BY_POST_ID_SQL,
                    (rs, rowNum) -> rs.getString("tag"),
                    post.getId()
            );
            post.getTags().clear();
            post.getTags().addAll(allTags);
        }

        return new ArrayList<>(postMap.values()).reversed();
    }

    @Override
    @Transactional
    public void updatePost(Post post) {
        jdbcTemplate.update(UPDATE_POST_SQL, post.getName(), post.getText(), post.getImage(), post.getId());
        var keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(DELETE_TAGS_SQL, post.getId());

        for (String tag : post.getTags()) {
            Long tagId = jdbcTemplate.query(FIND_TAG_SQL, rs -> {
                if (rs.next()) {
                    return rs.getLong("id");
                }
                return null;
            }, tag);

            if (tagId == null) {
                jdbcTemplate.update(con -> {
                    PreparedStatement ps = con.prepareStatement(CREATE_TAG_SQL, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, tag);
                    return ps;
                }, keyHolder);
                tagId = (Long) keyHolder.getKeys().get("id");
            }

            jdbcTemplate.update(TAGS_POST_SQL, post.getId(), tagId);
        }
    }

    @Override
    public Optional<Post> findById(Long postId) {
        Map<Long, Post> postMap = new HashMap<>();

        jdbcTemplate.query(FIND_POST_BY_ID_SQL, rs -> {
            Post post = postMap.computeIfAbsent(rs.getLong("id"), id -> {
                try {
                    return Post.builder()
                            .id(rs.getLong("id"))
                            .name(rs.getString("name"))
                            .image(rs.getBytes("image"))
                            .text(rs.getString("text"))
                            .likes(rs.getInt("likes"))
                            .tags(new ArrayList<>())
                            .build();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

            String tag = rs.getString("tag");
            if (tag != null && !post.getTags().contains(tag)) {
                post.getTags().add(tag);
            }
        }, postId);

        return new ArrayList<>(postMap.values()).stream().findFirst();
    }

    @Override
    public boolean existById(Long postId) {
        return jdbcTemplate.queryForObject(EXIST_POST_SQL, Boolean.class, postId);
    }

    @Override
    public List<Comment> findAllCommentsByPostId(Long postId) {
        return jdbcTemplate.query(FIND_ALL_COMMENTS, (rs, rowNum) ->
                Comment.builder()
                        .id(rs.getLong("id"))
                        .text(rs.getString("text"))
                        .postId(postId)
                        .build(), postId);
    }

    @Override
    @Transactional
    public void createPost(Post post) {
        var keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(CREATE_POST_SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, post.getName());
            ps.setString(2, post.getText());
            ps.setBytes(3, post.getImage());
            return ps;
        }, keyHolder);

        var postId = keyHolder.getKeys().get("id");

        for (String tag : post.getTags()) {
            Long tagId = jdbcTemplate.query(FIND_TAG_SQL, rs -> {
                if (rs.next()) {
                    return rs.getLong("id");
                }
                return null;
            }, tag);

            if (tagId == null) {
                jdbcTemplate.update(con -> {
                    PreparedStatement ps = con.prepareStatement(CREATE_TAG_SQL, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, tag);
                    return ps;
                }, keyHolder);
                tagId = (Long) keyHolder.getKeys().get("id");
            }

            jdbcTemplate.update(TAGS_POST_SQL, postId, tagId);
        }
    }

    @Override
    public Integer addLike(Long postId) {
        var keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(ADD_LIKE_SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, postId);
            return ps;
        }, keyHolder);

        var likes = keyHolder.getKeys().get("likes");

        return (Integer) likes;
    }

    @Override
    public void deletePost(Long postId) {
        jdbcTemplate.update(DELETE_POST_SQL, postId);
    }

    @Override
    public String addComment(Long postId, String text) {
        var keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(ADD_COMMENT_SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, text);
            ps.setLong(2, postId);
            return ps;
        }, keyHolder);

        var comment = keyHolder.getKeys().get("text");

        return (String) comment;
    }

    @Override
    public void updateComment(Long commentId, String text) {
        jdbcTemplate.update(UPDATE_COMMENT_SQL, text, commentId);
    }

    @Override
    public void deleteComment(Long commentId) {
        jdbcTemplate.update(DELETE_COMMENT_SQL, commentId);
    }
}
