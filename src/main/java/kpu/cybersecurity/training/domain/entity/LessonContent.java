package kpu.cybersecurity.training.domain.entity;

import jakarta.persistence.*;
import kpu.cybersecurity.training.domain.dto.request.ReqLessonDTO;
import kpu.cybersecurity.training.domain.dto.response.ResLessonContentDTO;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LessonContent implements Mapper<LessonContent, ResLessonContentDTO, ReqLessonDTO> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lessonContentId;

    @Column(columnDefinition = "LONGTEXT", nullable = true)
    private String content;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "lesson_id", nullable = false, unique = true)
    private Lesson lesson;

    @Override
    public ResLessonContentDTO toResponseDto() {
        ResLessonContentDTO dto = new ResLessonContentDTO();

        dto.setLesson(this.getLesson().toResponseDto());
        dto.setLessonContentId(this.getLessonContentId());
        dto.setContent(this.getContent());

        return dto;
    }

    @Override
    public void fromRequestDto(ReqLessonDTO dto) {
        this.setContent(dto.getContent() != null? dto.getContent() : null);
    }
}


