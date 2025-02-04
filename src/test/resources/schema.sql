CREATE TABLE IF NOT EXISTS posts
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL UNIQUE,
    name  VARCHAR(255)                            NOT NULL,
    text  VARCHAR(2550)                           NOT NULL,
    image BYTEA NOT NULL,
    likes INTEGER DEFAULT 0,
    CONSTRAINT pk_post PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS comments
(
    id      BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL UNIQUE,
    text    VARCHAR(2550)                           NOT NULL,
    post_id BIGINT                                  NOT NULL,
    CONSTRAINT pk_comment PRIMARY KEY (id),
    FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tags
(
    id  BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL UNIQUE,
    tag VARCHAR(55)                             NOT NULL,
    CONSTRAINT pk_tag PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS tags_posts
(
    post_id BIGINT NOT NULL,
    tag_id  BIGINT NOT NULL,
    CONSTRAINT pk_tag_post PRIMARY KEY (post_id, tag_id),
    FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tags (id) ON DELETE CASCADE
);