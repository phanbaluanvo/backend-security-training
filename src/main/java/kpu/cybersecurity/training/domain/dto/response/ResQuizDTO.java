package kpu.cybersecurity.training.domain.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class ResQuizDTO {
    private Long quizId;
    private String quizName;
    private String description;
    private boolean active;
    private ResCourseDTO course;
    private List<ResQuestionDTO> questions;
    private String createdBy;
    private Instant createdAt;
    private String updatedBy;
    private Instant updatedAt;
}