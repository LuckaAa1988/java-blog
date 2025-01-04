package ru.practicum.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({WebConfig.class,
        DataSourceConfig.class})
public class AppConfig {
}
