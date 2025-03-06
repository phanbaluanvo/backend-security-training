package kpu.cybersecurity.training.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseCookie;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResLoginWithCookieDTO {
    private ResLoginDTO userLogin;
    private ResponseCookie cookie;
}
