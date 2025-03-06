package kpu.cybersecurity.training.domain.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResLessonDTO {
    private Long lessonId;
    private String lessonName;
    private ResModuleDTO module;
    private ResLessonContentDTO lessonContent;
}
