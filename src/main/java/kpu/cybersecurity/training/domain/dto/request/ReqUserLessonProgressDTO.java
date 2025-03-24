package kpu.cybersecurity.training.domain.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqUserLessonProgressDTO {
    private Long lessonId;
    private Long courseId;
    private Double completePercentage;
}
