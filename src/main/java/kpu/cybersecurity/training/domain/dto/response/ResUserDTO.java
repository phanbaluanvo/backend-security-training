package kpu.cybersecurity.training.domain.dto.response;

import kpu.cybersecurity.training.domain.enums.Role;
import lombok.Data;

import java.time.Instant;

@Data
public class ResUserDTO {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private Instant createdAt;
    private Instant updatedAt;
}
