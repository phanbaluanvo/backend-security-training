package kpu.cybersecurity.training.domain.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqAnswerDTO {
    private String answerText;
    private boolean correct;
}