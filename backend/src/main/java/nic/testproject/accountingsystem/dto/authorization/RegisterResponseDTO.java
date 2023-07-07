package nic.testproject.accountingsystem.dto.authorization;

import lombok.Builder;
import lombok.Data;

@Data
public class RegisterResponseDTO {
    private String firstName;
    private String secondName;
    private String middleName;
}
