package ru.practicum.model;

import lombok.*;

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
    String text;
}
