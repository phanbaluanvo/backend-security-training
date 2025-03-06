package kpu.cybersecurity.training.domain.entity;

import jakarta.persistence.*;
import kpu.cybersecurity.training.domain.dto.request.ReqUserDTO;
import kpu.cybersecurity.training.domain.dto.response.ResUserDTO;
import kpu.cybersecurity.training.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Mapper<User, ResUserDTO, ReqUserDTO> {
    @Id
    private String userId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String password;

    @Enumerated(EnumType.STRING)
    @Column
    private Role role;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String refreshToken;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = true)
    private Instant updatedAt;

    @Override
    public ResUserDTO toResponseDto() {
        ResUserDTO dto = new ResUserDTO();

        dto.setUserId(this.getUserId());
        dto.setFirstName(this.getFirstName().trim());
        dto.setLastName(this.getLastName().trim());
        dto.setEmail(this.getEmail().trim());
        dto.setRole(this.getRole());
        dto.setCreatedAt(this.getCreatedAt());
        dto.setUpdatedAt(this.getUpdatedAt());

        return dto;
    }

    @Override
    public void fromRequestDto(ReqUserDTO dto) {
        this.setUserId(dto.getUserId());
        this.setFirstName(dto.getFirstName().trim());
        this.setLastName(dto.getLastName().trim());
        this.setEmail(dto.getEmail().trim());
        this.setRole(dto.getRole());
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();

    }
}
