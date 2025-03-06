package kpu.cybersecurity.training.domain.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ResCourseDTO {
    private Long courseId;
    private String courseName;
    private String description;
    private ResTopicDTO topic;
    private String createdBy;
    private Instant createdAt;
    private String updatedBy;
    private Instant updatedAt;
}
