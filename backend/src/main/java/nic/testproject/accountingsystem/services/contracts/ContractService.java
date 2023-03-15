package nic.testproject.accountingsystem.services.contracts;

import nic.testproject.accountingsystem.dto.contracts.ContractDTO;
import nic.testproject.accountingsystem.models.contracts.Contract;
import nic.testproject.accountingsystem.models.contracts.counterparty.ContractCounterparties;
import nic.testproject.accountingsystem.models.contracts.details.ContractPhase;
import nic.testproject.accountingsystem.repositories.contracts.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ContractService {

    private final ContractRepository contractRepository;

    @Autowired
    public ContractService(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    public Page<Contract> findContracts(ContractDTO criteria, Pageable pageable) {
        System.out.println(ContractSpecifications.searchContracts(criteria));
        return contractRepository.findAll(ContractSpecifications.searchContracts(criteria), pageable);
    }

    public void addContract(Contract contractDTO) {

        List<ContractPhase> contractPhase = contractDTO.getPhases();
        List<ContractCounterparties> contractCounterparties = contractDTO.getContractCounterparties();

        Contract contract = Contract.builder()
                .name(contractDTO.getName())
                .type(contractDTO.getType())
                .actualEndDate(contractDTO.getActualEndDate())
                .actualStartDate(contractDTO.getActualStartDate())
                .amount(contractDTO.getAmount())
                .plannedEndDate(contractDTO.getPlannedEndDate())
                .plannedStartDate(contractDTO.getPlannedStartDate())

                .phases(contractPhase)
                .contractCounterparties(contractCounterparties)

                .build();

        contractRepository.save(contract);

        contractPhase.forEach(it -> {
            it.setContract(contract);
            it.getExpenses().forEach(expense -> expense.setContractPhase(it));
        });
        contractCounterparties.forEach(it -> it.setMainContract(contract));
    }
}
