package nic.testproject.accountingsystem.dto.administration;

import lombok.Data;
import nic.testproject.accountingsystem.models.user.Role;

import java.util.List;

@Data
public class UserDTO {
    private String firstName;
    private String secondName;
    private String middleName;
    private String username;
    private List<Role> roles;
    private String expireDate;
}
