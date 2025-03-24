package kpu.cybersecurity.training.domain.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ResModuleLessonProgressDTO {
    private Long moduleId;
    private String moduleName;
    private List<ResLessonProgressDTO> lessons;
    public ResModuleLessonProgressDTO(Long moduleId, String moduleName) {
        this.moduleId = moduleId;
        this.moduleName = moduleName;
        this.lessons = new ArrayList<>();
    }

}