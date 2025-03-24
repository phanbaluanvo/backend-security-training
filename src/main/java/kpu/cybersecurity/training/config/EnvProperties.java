package kpu.cybersecurity.training.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "variables")
@Log4j2
public class EnvProperties {
    private Jwt jwt;
    private JsonFormat jsonFormat;
    private Aws aws;

    @PostConstruct
    public void init() {
        log.info("Loaded JWT Secret: {}", jwt.getBase64Secret());

    }

    @Getter
    @Setter
    public static class Jwt {
        private String base64Secret;
        private long accessTokenValidTime;
        private long refreshTokenValidTime;
    }

    @Getter
    @Setter
    public static class JsonFormat {
        private String dateTime;
        private String timeZone;
    }

    @Getter
    @Setter
    public static class Aws {
        private String accessKey;
        private String secretKey;
        private String region;
        private S3 s3;

        @Getter
        @Setter
        public static class S3 {
            private String bucket;
        }
    }
}