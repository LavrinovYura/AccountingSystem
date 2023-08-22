package nic.testproject.accountingsystem.dto.authorization;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class JwtResponse {
    String accessToken;
    String refreshToken;
}
