package ru.practicum.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@ToString
public class Post {
    Long id;
    String name;
    String image;
    String text;
    List<String> tags;
    Integer likes;
    Integer commentsCount;
}
