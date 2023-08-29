package nic.testproject.accountingsystem.dto.contracts;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@EqualsAndHashCode(callSuper = true)
@Data
public class ContractCounterpartiesDTO extends AbstractContract{

    private Long id;

    private CounterpartyDTO counterparty;

}
