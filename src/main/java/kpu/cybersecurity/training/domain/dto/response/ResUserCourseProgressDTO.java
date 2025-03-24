package kpu.cybersecurity.training.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ResUserCourseProgressDTO {
    private String userId;
    private Long courseId;
    private String courseName;
    private boolean complete;
    private LocalDate completionDate;
    private List<ResModuleLessonProgressDTO> modules;


    public ResUserCourseProgressDTO(String userId, Long courseId, String courseName, boolean isComplete, LocalDate completionDate) {
        this.userId = userId;
        this.courseId = courseId;
        this.courseName = courseName;
        this.complete = isComplete;
        this.completionDate = completionDate;
        this.modules = new ArrayList<>();
    }

}
