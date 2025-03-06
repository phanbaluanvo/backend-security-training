package kpu.cybersecurity.training.domain.entity;

import jakarta.persistence.*;
import kpu.cybersecurity.training.domain.dto.request.ReqCourseDTO;
import kpu.cybersecurity.training.domain.dto.response.ResCourseDTO;
import kpu.cybersecurity.training.util.SecurityUtil;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course implements Mapper<Course, ResCourseDTO, ReqCourseDTO> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    @Column(nullable = false, unique = true)
    private String courseName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = true)
    private Topic topic;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Module> modules = new ArrayList<>();

    @Column
    private String createdBy;

    @Column
    private Instant createdAt;

    @Column
    private String updatedBy;

    @Column
    private Instant updatedAt;

    @Override
    public ResCourseDTO toResponseDto() {
        ResCourseDTO dto = new ResCourseDTO();

        dto.setCourseId(this.getCourseId());
        dto.setCourseName(this.getCourseName().trim());
        dto.setTopic(this.getTopic() != null? this.getTopic().toResponseDto() : null);
        dto.setDescription(this.getDescription());
        dto.setCreatedBy(this.getCreatedBy());
        dto.setCreatedAt(this.getCreatedAt());
        dto.setUpdatedBy(this.getUpdatedBy());
        dto.setUpdatedAt(this.getUpdatedAt());

        return dto;
    }

    @Override
    public void fromRequestDto(ReqCourseDTO dto) {
        this.setCourseName(dto.getCourseName().trim());
        this.setDescription(dto.getDescription());

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

