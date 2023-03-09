package nic.testproject.accountingsystem.dto.authorization;

import lombok.Data;

@Data
public class RegisterDTO {
    private String username;
    private String password;
    private String fullName;
}
