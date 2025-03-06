package kpu.cybersecurity.training.domain.dto.request;

import kpu.cybersecurity.training.domain.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqUserDTO {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private String password;
}
