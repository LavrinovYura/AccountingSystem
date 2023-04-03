package nic.testproject.accountingsystem.repositories.contracts.projections.update;

import lombok.Data;
import nic.testproject.accountingsystem.models.contracts.details.ContractCounterparties;

import java.util.List;

@Data
public class UpdateCounterpartyDTO {
    private String name;
    private String address;
    private String inn;
}
