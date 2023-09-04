package nic.testproject.accountingsystem.dtos.contracts;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
public class ContractCounterpartyDTO extends AbstractContract{

    private Long id;

    @Valid

    @NotNull(message = "Counterparty id needed")
    private Long counterpartyId;
}
