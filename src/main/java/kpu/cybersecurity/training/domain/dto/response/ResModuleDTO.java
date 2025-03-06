package kpu.cybersecurity.training.domain.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ResModuleDTO {
    private Long moduleId;
    private String moduleName;
    private String description;
    private ResCourseDTO course;
    private String createdBy;
    private Instant createdAt;
    private String updatedBy;
    private Instant updatedAt;
}
