CREATE TABLE IF NOT EXISTS posts
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL UNIQUE,
    name  VARCHAR(255)                            NOT NULL,
    text  VARCHAR(25500)                           NOT NULL,
    image VARCHAR(2550)  NOT NULL,
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


INSERT INTO tags (tag)
VALUES ('космос'),
       ('Марс'),
       ('экзопланеты'),
       ('Луна'),
       ('NASA'),
       ('SpaceX'),
       ('наука'),
       ('астрономия'),
       ('технологии'),
       ('вселенная');

INSERT INTO posts (name, text, image, likes)
VALUES ('Загадки тёмной материи',
        'Учёные до сих пор не знают, из чего состоит 85% Вселенной. Тёмная материя остаётся одной из главных загадок космоса.','/images/2e92c3f2-3d76-45b2-b651-7a14d7231a60.jpg',7),
       ('Красная планета',
        'Марс — четвёртая планета Солнечной системы. Учёные предполагают, что когда-то там была вода.','/images/2e92c3f2-3d76-45b2-b651-7a14d7231a60.jpg',1),
       ('Черные дыры', 'Черные дыры настолько плотные, что даже свет не может покинуть их пределы.','/images/2e92c3f2-3d76-45b2-b651-7a14d7231a60.jpg',4),
       ('Млечный путь', 'Наша галактика содержит около 200 миллиардов звёзд. Она простирается на 100 000 световых лет.','/images/2e92c3f2-3d76-45b2-b651-7a14d7231a60.jpg',5),
       ('Колонизация Марса',
        'Марс — главный кандидат для колонизации человеком. Его атмосфера на 95% состоит из углекислого газа, а температура колеблется от -125 °C до 20 °C. Компании, такие как SpaceX, разрабатывают ракеты и технологии для транспортировки людей и оборудования на Марс. Вопросы создания систем жизнеобеспечения и защиты от радиации остаются ключевыми.','/images/2e92c3f2-3d76-45b2-b651-7a14d7231a60.jpg',11),
       ('Космическая радиация',
        'За пределами земной атмосферы уровень радиации значительно выше. Это одна из главных проблем для длительных миссий в космосе, например, на Марс. Ученые разрабатывают новые технологии защиты.','/images/2e92c3f2-3d76-45b2-b651-7a14d7231a60.jpg',0),
       ('Экзопланеты',
        'За последние десятилетия было открыто более 5000 экзопланет. Некоторые из них находятся в «обитаемой зоне», где возможно наличие воды в жидком состоянии.','/images/2e92c3f2-3d76-45b2-b651-7a14d7231a60.jpg',10),
       ('Спутники Земли',
        'Помимо Луны, Земля имеет тысячи искусственных спутников. Они используются для связи, наблюдений, навигации и научных исследований.','/images/2e92c3f2-3d76-45b2-b651-7a14d7231a60.jpg',2),
       ('Большой взрыв',
        'Вселенная возникла около 13,8 миллиарда лет назад в результате Большого взрыва. Этот момент считается началом пространства, времени и материи.','/images/2e92c3f2-3d76-45b2-b651-7a14d7231a60.jpg',4),
       ('Будущее межзвёздных путешествий',
        'Межзвёздные полёты пока остаются фантастикой, но разработки в области термоядерного синтеза и солнечных парусов дают надежду на их реализацию. Такие проекты, как Breakthrough Starshot, предполагают запуск нанозондов к системе Альфа Центавра. Этот шаг может стать началом исследования далёких звёздных систем','/images/2e92c3f2-3d76-45b2-b651-7a14d7231a60.jpg',3),
       ('Орбитальные телескопы',
        'Телескопы, такие как «Хаббл» и «Джеймс Уэбб», позволяют заглянуть в прошлое Вселенной и увидеть галактики, существовавшие миллиарды лет назад.','/images/2e92c3f2-3d76-45b2-b651-7a14d7231a60.jpg',2),
       ('Полёт на Луну',
        'В 1969 году Нил Армстронг стал первым человеком, ступившим на Луну. Его знаменитые слова: «Это один маленький шаг для человека, но гигантский скачок для человечества».','/images/2e92c3f2-3d76-45b2-b651-7a14d7231a60.jpg',10),
       ('Гравитационные волны',
        'В 2015 году впервые зафиксированы гравитационные волны, предсказанные Эйнштейном 100 лет назад. Они открыли новое окно в изучении Вселенной.','/images/2e92c3f2-3d76-45b2-b651-7a14d7231a60.jpg',10),
       ('Солнечная система',
        'В нашей системе восемь планет, пять карликовых планет и миллионы астероидов. Она образовалась более 4,6 миллиарда лет назад.','/images/2e92c3f2-3d76-45b2-b651-7a14d7231a60.jpg',1),
       ('Миссия «Вояджер»',
        '«Вояджер-1» — самый удалённый объект, созданный человеком. Он преодолел более 23 миллиардов километров.','/images/2e92c3f2-3d76-45b2-b651-7a14d7231a60.jpg',33);

INSERT INTO comments (text, post_id)
VALUES ('Отличный пост!', 1),
       ('Очень полезно', 1),
       ('Мне понравилась эта статья', 2),
       ('Хорошее объяснение!', 3),
       ('Прекрасный контент!', 4),
       ('Потрясающе!', 5),
       ('Интересная тема', 6),
       ('Спасибо, что поделились', 7),
       ('Я узнал много нового', 8),
       ('Очень информативно', 9),
       ('Чётко и понятно', 10),
       ('Отличная работа!', 11),
       ('Удивительные идеи', 12),
       ('Полезные примеры', 13),
       ('Продолжайте в том же духе!', 14),
       ('Превосходный контент!', 15);


INSERT INTO tags_posts (post_id, tag_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (2, 4),
       (2, 5),
       (3, 6),
       (3, 7),
       (4, 8),
       (4, 9),
       (5, 10),
       (5, 1),
       (6, 2),
       (6, 3),
       (7, 4),
       (7, 5),
       (8, 6),
       (8, 7),
       (9, 8),
       (9, 9),
       (10, 10),
       (10, 1),
       (11, 2),
       (11, 3),
       (12, 4),
       (12, 5),
       (13, 6),
       (13, 7),
       (14, 8),
       (14, 9),
       (15, 10),
       (15, 1);
