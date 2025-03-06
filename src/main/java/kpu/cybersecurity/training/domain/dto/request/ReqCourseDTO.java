package kpu.cybersecurity.training.domain.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqCourseDTO {
    private String courseName;
    private String description;
    private Long topicId;
}
