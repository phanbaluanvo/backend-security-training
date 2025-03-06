package kpu.cybersecurity.training.domain.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqModuleDTO {
    private String moduleName;
    private String description;
    private Long courseId;
}
