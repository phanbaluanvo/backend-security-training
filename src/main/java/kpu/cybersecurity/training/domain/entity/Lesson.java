package kpu.cybersecurity.training.domain.entity;

import jakarta.persistence.*;
import kpu.cybersecurity.training.domain.dto.request.ReqLessonDTO;
import kpu.cybersecurity.training.domain.dto.response.ResLessonDTO;
import kpu.cybersecurity.training.util.SecurityUtil;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lesson implements Mapper<Lesson, ResLessonDTO, ReqLessonDTO> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lessonId;

    @Column(nullable = false)
    private String lessonName;

    @ManyToOne
    @JoinColumn(name = "module_id", nullable = true)
    private Module module;

    @Column
    private String createdBy;

    @Column
    private Instant createdAt;

    @Column
    private String updatedBy;

    @Column
    private Instant updatedAt;

    @Override
    public ResLessonDTO toResponseDto() {
        ResLessonDTO dto = new ResLessonDTO();

        dto.setLessonId(this.getLessonId());
        dto.setLessonName(this.getLessonName());
        dto.setModule(this.getModule() != null? this.getModule().toResponseDto() : null);

        return dto;
    }

    @Override
    public void fromRequestDto(ReqLessonDTO dto) {
        this.setLessonName(dto.getLessonName().trim());
    }

    @PrePersist
    protected void onCreate() {
        this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent()?
                SecurityUtil.getCurrentUserLogin().get() : "";
        this.createdAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent()?
                SecurityUtil.getCurrentUserLogin().get() : "";
        this.updatedAt = Instant.now();

    }
}
