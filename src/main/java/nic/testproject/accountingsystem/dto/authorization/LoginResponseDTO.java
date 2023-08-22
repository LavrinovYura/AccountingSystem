package nic.testproject.accountingsystem.dto.authorization;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String accessToken;
    private String refreshToken;
    private final String tokenType = "Bearer ";
    private String firstName;
    private String secondName;
    private String middleName;
}
