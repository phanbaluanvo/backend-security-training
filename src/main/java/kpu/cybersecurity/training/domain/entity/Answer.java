package kpu.cybersecurity.training.domain.entity;

import java.time.Instant;

import jakarta.persistence.*;
import kpu.cybersecurity.training.domain.dto.request.ReqAnswerDTO;
import kpu.cybersecurity.training.domain.dto.response.ResAnswerDTO;
import kpu.cybersecurity.training.util.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Answer implements Mapper<Answer, ResAnswerDTO, ReqAnswerDTO> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long answerId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String answerText;

    @Column(nullable = false)
    private boolean isCorrect = false;

    @ManyToOne
    @JoinColumn(name = "questionId", nullable = false)
    private Question question;

    @Column
    private String createdBy;

    @Column
    private Instant createdAt;

    @Column
    private String updatedBy;

    @Column
    private Instant updatedAt;

    @Override
    public ResAnswerDTO toResponseDto() {
        ResAnswerDTO dto = new ResAnswerDTO();
        dto.setAnswerId(this.getAnswerId());
        dto.setAnswerText(this.getAnswerText().trim());
        dto.setCorrect(this.isCorrect());
        dto.setQuestionId(this.getQuestion() != null ? this.getQuestion().getQuestionId() : null);
        return dto;
    }

    @Override
    public void fromRequestDto(ReqAnswerDTO dto) {
        this.setAnswerText(dto.getAnswerText().trim());
        this.setCorrect(dto.isCorrect());
    }

    @PrePersist
    protected void onCreate() {
        this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        this.createdAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        this.updatedAt = Instant.now();
    }
}
