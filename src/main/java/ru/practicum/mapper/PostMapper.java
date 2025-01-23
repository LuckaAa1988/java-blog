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
                .image(post.getImage())
                .shortText(getShortenedText(post.getText()))
                .likes(post.getLikes())
                .commentsCount(post.getCommentsCount())
                .tags(post.getTags())
                .build();
    }

    public FullPostDTO toFullDTO(Post post, List<Comment> comments) {
        return FullPostDTO.builder()
                .id(post.getId())
                .name(post.getName())
                .image(post.getImage())
                .text(post.getText())
                .likes(post.getLikes())
                .tags(post.getTags())
                .comments(comments.stream()
                        .map(commentMapper::toDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    private String getShortenedText(String text) {
        int maxLines  = 3;
        String[] lines = text.split("<br>");
        StringBuilder shortenedText = new StringBuilder();

        for (int i = 0; i < Math.min(lines.length, maxLines); i++) {
            if (lines[i].isBlank()) {
                break;
            }
            shortenedText.append(lines[i]).append("<br>");
        }
        return shortenedText.toString();
    }
}
