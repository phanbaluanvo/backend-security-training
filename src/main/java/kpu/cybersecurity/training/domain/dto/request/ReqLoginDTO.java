package kpu.cybersecurity.training.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqLoginDTO {

    @NotBlank(message = "User ID can not be null!")
    private String username;

    @NotBlank(message = "Password can not be null!")
    private String password;
}
