package kpu.cybersecurity.training.domain.entity;

import jakarta.persistence.*;
import kpu.cybersecurity.training.domain.dto.request.ReqTopicDTO;
import kpu.cybersecurity.training.domain.dto.response.ResTopicDTO;
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
public class Topic implements Mapper<Topic, ResTopicDTO, ReqTopicDTO> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long topicId;

    @Column(nullable = false)
    private String topicName;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL)
    private List<Question> questions = new ArrayList<>();

    @Column
    private String createdBy;

    @Column
    private Instant createdAt;

    @Column
    private String updatedBy;

    @Column
    private Instant updatedAt;

    @Override
    public ResTopicDTO toResponseDto() {
        ResTopicDTO dto = new ResTopicDTO();

        dto.setTopicId(this.getTopicId());
        dto.setTopicName(this.getTopicName().trim());
        dto.setCreatedBy(this.getCreatedBy());
        dto.setCreatedAt(this.getCreatedAt());
        dto.setUpdatedBy(this.getUpdatedBy());
        dto.setUpdatedAt(this.getUpdatedAt());

        return dto;
    }

    @Override
    public void fromRequestDto(ReqTopicDTO dto) {
        this.setTopicName(dto.getTopicName().trim());
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
