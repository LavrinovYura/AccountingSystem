package nic.testproject.accountingsystem.dto.contracts;

import lombok.Data;
import lombok.EqualsAndHashCode;
import nic.testproject.accountingsystem.models.contracts.details.Counterparty;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
public class ContractCounterpartyDTO extends AbstractContract{

    private Long id;

    private Long counterpartyId;

    private Counterparty counterparty;

}
