package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.CommentDTO;
import ru.practicum.model.Comment;

@Component
public class CommentMapper {

    public CommentDTO toDTO(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .text(comment.getText())
                .build();
    }
}
