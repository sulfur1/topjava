package config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"ru.javawebinar.topjava.repository.inmemory"})
public class TestConfig {
}
