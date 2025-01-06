package ru.practicum.model;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@ToString
public class Comment {
    Long id;
    String text;
    Long postId;
}
