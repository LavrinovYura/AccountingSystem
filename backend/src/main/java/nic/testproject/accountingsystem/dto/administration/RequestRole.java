package nic.testproject.accountingsystem.dto.administration;

import lombok.Data;
import nic.testproject.accountingsystem.models.user.RoleType;

@Data
public class RequestRole {
    String roleType;
    String name;
}
