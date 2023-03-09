package nic.testproject.accountingsystem.dto.authorization;

import lombok.Data;

@Data
public class AuthResponseDTO {
    public String accessToken;
    public String tokenType = "Bearer ";

    public AuthResponseDTO(String accessToken){
        this.accessToken = accessToken;
    }
}
