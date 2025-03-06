package kpu.cybersecurity.training.domain.dto.response;

import kpu.cybersecurity.training.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ResLoginDTO {
    private String accessToken;
    private UserLogin user;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserLogin {
        private String userId;
        private String firstName;
        private String lastName;
        private String email;
        private Role role;
    }
}
