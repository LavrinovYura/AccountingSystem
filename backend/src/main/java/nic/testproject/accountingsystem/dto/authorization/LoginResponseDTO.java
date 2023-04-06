package nic.testproject.accountingsystem.dto.authorization;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String accessToken;
    private String tokenType = "Bearer ";
    private String fullName;

    public LoginResponseDTO(String accessToken, String fullName){
        this.accessToken = accessToken;
        this.fullName = fullName;
    }
}
