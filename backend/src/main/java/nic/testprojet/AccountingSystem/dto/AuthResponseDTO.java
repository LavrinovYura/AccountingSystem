package nic.testprojet.AccountingSystem.dto;

import lombok.Data;

@Data
public class AuthResponseDTO {
    public String accessToken;
    public String tokenType = "Bearer ";

    public AuthResponseDTO(String accessToken){
        this.accessToken = accessToken;
    }
}
