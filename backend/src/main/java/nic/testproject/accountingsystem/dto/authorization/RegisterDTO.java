package nic.testproject.accountingsystem.dto.authorization;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class RegisterDTO {
    private String username;
    private String password;
    private String firstName;
    private String secondName;
    private String middleName;
}
