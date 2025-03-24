package kpu.cybersecurity.training.domain.entity;

import jakarta.persistence.*;
import kpu.cybersecurity.training.domain.dto.request.ReqQuizDTO;
import kpu.cybersecurity.training.domain.dto.response.ResQuestionDTO;
import kpu.cybersecurity.training.domain.dto.response.ResQuizDTO;
import kpu.cybersecurity.training.util.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quiz implements Mapper<Quiz, ResQuizDTO, ReqQuizDTO> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    private Long quizId;

    @Column(nullable = false)
    private String quizName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuizQuestion> quizQuestions = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private boolean isActive = false;

    @Column
    private String createdBy;

    @Column
    private Instant createdAt;

    @Column
    private String updatedBy;

    @Column
    private Instant updatedAt;

    @Override
    public ResQuizDTO toResponseDto() {
        ResQuizDTO dto = new ResQuizDTO();
        dto.setQuizId(this.getQuizId());
        dto.setQuizName(this.getQuizName().trim());
        dto.setDescription(this.getDescription());
        dto.setActive(this.isActive());
        dto.setCourse(this.getCourse() != null ? this.getCourse().toResponseDto() : null);

        if (this.getQuizQuestions() != null) {
            List<ResQuestionDTO> questions = this.getQuizQuestions().stream()
                    .map(qq -> qq.getQuestion().toResponseDto())
                    .collect(Collectors.toList());
            dto.setQuestions(questions);
        } else {
            dto.setQuestions(new ArrayList<>());
        }

        dto.setCreatedBy(this.getCreatedBy());
        dto.setCreatedAt(this.getCreatedAt());
        dto.setUpdatedBy(this.getUpdatedBy());
        dto.setUpdatedAt(this.getUpdatedAt());
        return dto;
    }

    @Override
    public void fromRequestDto(ReqQuizDTO dto) {
        this.setQuizName(dto.getQuizName().trim());
        this.setDescription(dto.getDescription());
        this.setActive(dto.isActive());
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
