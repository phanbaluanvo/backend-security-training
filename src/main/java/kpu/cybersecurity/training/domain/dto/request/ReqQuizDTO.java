package kpu.cybersecurity.training.domain.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReqQuizDTO {
    private String quizName;
    private String description;
    private boolean active;
    private Long courseId;
    private List<ReqQuestionDTO> questions;
}