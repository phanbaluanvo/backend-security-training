package kpu.cybersecurity.training.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResLessonProgressDTO {
    private Long lessonId;
    private String lessonName;
    private boolean complete;
}
