package ru.practicum.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.dto.CreatePostDTO;
import ru.practicum.dto.FullPostDTO;
import ru.practicum.dto.PreviewPostDTO;
import ru.practicum.model.Comment;
import ru.practicum.model.Post;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PostMapper {

    private final CommentMapper commentMapper;

    public PreviewPostDTO toPreviewDTO(Post post) {
        return PreviewPostDTO.builder()
                .id(post.getId())
                .name(post.getName())
                .image(Base64.getMimeEncoder().encodeToString(post.getImage()))
                .shortText(post.getText())
                .likes(post.getLikes())
                .commentsCount(post.getCommentsCount())
                .tags(post.getTags())
                .build();
    }

    public FullPostDTO toFullDTO(Post post, List<Comment> comments) {
        return FullPostDTO.builder()
                .id(post.getId())
                .name(post.getName())
                .image(Base64.getMimeEncoder().encodeToString(post.getImage()))
                .text(post.getText())
                .likes(post.getLikes())
                .tags(post.getTags())
                .comments(comments.stream()
                        .map(commentMapper::toDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    public Post fromDTO(CreatePostDTO postDTO) {
        return Post.builder()
                .name(postDTO.getName())
                .text(postDTO.getText())
                .image(postDTO.getImage())
                .tags(postDTO.getTags())
                .build();
    }
}
