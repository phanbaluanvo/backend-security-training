package kpu.cybersecurity.training.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import kpu.cybersecurity.training.config.EnvProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class CustomInstantSerializer extends JsonSerializer<Instant> {

    private final DateTimeFormatter FORMATTER;

    public CustomInstantSerializer(EnvProperties envProperties) {
        this.FORMATTER = DateTimeFormatter.ofPattern(envProperties.getJsonFormat().getDateTime())
                        .withZone(ZoneId.of(envProperties.getJsonFormat().getTimeZone()));
    }

    @Override
    public void serialize(Instant value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(FORMATTER.format(value));
    }
}
