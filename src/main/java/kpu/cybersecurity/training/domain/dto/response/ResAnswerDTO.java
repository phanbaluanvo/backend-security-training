package kpu.cybersecurity.training.domain.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResAnswerDTO {
    private Long questionId;
    private Long answerId;
    private String answerText;
    private boolean correct;
}