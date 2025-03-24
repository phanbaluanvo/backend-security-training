package kpu.cybersecurity.training.domain.dto.request;

import kpu.cybersecurity.training.domain.enums.Difficulty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReqQuestionDTO {
    private String questionText;
    private Difficulty difficulty;
    private List<ReqAnswerDTO> answers;
}