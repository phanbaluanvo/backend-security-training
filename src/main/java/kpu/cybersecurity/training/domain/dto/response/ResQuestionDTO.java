package kpu.cybersecurity.training.domain.dto.response;

import kpu.cybersecurity.training.domain.enums.Difficulty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResQuestionDTO {
    private Long questionId;
    private String questionText;
    private Difficulty difficulty;
    private List<ResAnswerDTO> answers;
}