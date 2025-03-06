package kpu.cybersecurity.training.config;


import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kpu.cybersecurity.training.util.CustomInstantSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;

@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer(CustomInstantSerializer customInstantSerializer) {
        return builder -> {
            JavaTimeModule module = new JavaTimeModule();
            module.addSerializer(Instant.class, customInstantSerializer);
            builder.modules(module);
        };
    }
}
