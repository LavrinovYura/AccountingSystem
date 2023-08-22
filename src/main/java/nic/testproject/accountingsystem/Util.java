package nic.testproject.accountingsystem;

import nic.testproject.accountingsystem.models.contracts.Contract;
import nic.testproject.accountingsystem.models.contracts.details.ContractCounterparties;
import nic.testproject.accountingsystem.models.contracts.details.ContractPhase;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Util {

    public void linkContractIdToContractPhase(List<ContractPhase> contractPhase, Contract savedContract) {
        contractPhase.forEach(it -> it.setContract1(savedContract));
    }

    public void linkContractIdToContractCounterparties(List<ContractCounterparties> contractCounterparties, Contract savedContract) {
        contractCounterparties.forEach(it -> it.setContract2(savedContract));
    }
}
