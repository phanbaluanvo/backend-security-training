package kpu.cybersecurity.training.domain.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqLessonDTO {
    private String lessonName;
    private Long moduleId;
    private String content;
}
