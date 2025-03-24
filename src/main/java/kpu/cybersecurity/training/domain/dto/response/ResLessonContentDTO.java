package kpu.cybersecurity.training.domain.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResLessonContentDTO {
    private ResLessonDTO lesson;
    private Long lessonContentId;
    private String content;
}
