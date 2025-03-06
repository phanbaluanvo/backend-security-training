package kpu.cybersecurity.training.domain.entity;

import jakarta.persistence.*;
import kpu.cybersecurity.training.domain.dto.request.ReqModuleDTO;
import kpu.cybersecurity.training.domain.dto.response.ResModuleDTO;
import kpu.cybersecurity.training.util.SecurityUtil;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Module implements Mapper<Module, ResModuleDTO, ReqModuleDTO> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long moduleId;

    @Column(nullable = false, unique = true)
    private String moduleName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = true)
    private Course course;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lesson> lessons;

    @Column
    private String createdBy;

    @Column
    private Instant createdAt;

    @Column
    private String updatedBy;

    @Column
    private Instant updatedAt;

    @Override
    public ResModuleDTO toResponseDto() {
        ResModuleDTO dto = new ResModuleDTO();

        dto.setModuleId(this.getModuleId());
        dto.setModuleName(this.getModuleName().trim());
        dto.setDescription(this.getDescription());
        dto.setCourse(this.getCourse() != null? this.getCourse().toResponseDto() : null);
        dto.setCreatedBy(this.getCreatedBy());
        dto.setCreatedAt(this.getCreatedAt());
        dto.setUpdatedBy(this.getUpdatedBy());
        dto.setUpdatedAt(this.getUpdatedAt());

        return dto;
    }

    @Override
    public void fromRequestDto(ReqModuleDTO dto) {
        this.setModuleName(dto.getModuleName().trim());
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
