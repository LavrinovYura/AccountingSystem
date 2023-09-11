package nic.testproject.accountingsystem.dtos.authorization;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginDTO {
    @NotBlank(message = "Enter your username")
    private String username;

    @NotBlank(message = "Enter your password")
    private String password;
}
