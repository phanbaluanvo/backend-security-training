package kpu.cybersecurity.training.domain.entity;

import jakarta.persistence.*;
import kpu.cybersecurity.training.domain.dto.request.ReqQuestionDTO;
import kpu.cybersecurity.training.domain.dto.response.ResQuestionDTO;
import kpu.cybersecurity.training.domain.enums.Difficulty;
import kpu.cybersecurity.training.util.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question implements Mapper<Question, ResQuestionDTO, ReqQuestionDTO> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long questionId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Answer> answers = new ArrayList<>();

    @Column
    private String createdBy;

    @Column
    private Instant createdAt;

    @Column
    private String updatedBy;

    @Column
    private Instant updatedAt;

    @Override
    public ResQuestionDTO toResponseDto() {
        ResQuestionDTO dto = new ResQuestionDTO();
        dto.setQuestionId(this.getQuestionId());
        dto.setQuestionText(this.getQuestionText().trim());
        dto.setDifficulty(this.getDifficulty());
        dto.setAnswers(this.getAnswers() != null ? this.getAnswers().stream().map(Answer::toResponseDto).toList()
                : new ArrayList<>());
        return dto;
    }

    @Override
    public void fromRequestDto(ReqQuestionDTO dto) {
        this.setQuestionText(dto.getQuestionText().trim());
        this.setDifficulty(dto.getDifficulty());
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
