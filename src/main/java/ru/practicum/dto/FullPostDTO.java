package ru.practicum.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FullPostDTO {
    Long id;
    String name;
    String image;
    String text;
    List<CommentDTO> comments;
    List<String> tags;
    Integer likes;
}
