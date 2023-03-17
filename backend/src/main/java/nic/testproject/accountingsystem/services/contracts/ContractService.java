package nic.testproject.accountingsystem.services.contracts;

import nic.testproject.accountingsystem.dto.contracts.ContractDTO;
import nic.testproject.accountingsystem.models.contracts.Contract;
import nic.testproject.accountingsystem.models.contracts.counterparty.ContractCounterparties;
import nic.testproject.accountingsystem.models.contracts.details.ContractPhase;
import nic.testproject.accountingsystem.repositories.contracts.ContractRepository;
import nic.testproject.accountingsystem.services.contracts.specs.ContractSpecifications;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ContractService {

    private final ContractRepository contractRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ContractService(ContractRepository contractRepository, ModelMapper modelMapper) {
        this.contractRepository = contractRepository;
        this.modelMapper = modelMapper;
    }

    public Page<Contract> findContracts(ContractDTO criteria, Pageable pageable) {
        return contractRepository.findAll(ContractSpecifications.searchContracts(criteria), pageable);
    }

    public void saveContract(ContractDTO contractDTO) {

        List<ContractPhase> contractPhase = contractDTO.getPhases();
        List<ContractCounterparties> contractCounterparties = contractDTO.getContractCounterparties();

        Contract contract = modelMapper.map(contractDTO, Contract.class);
        contractRepository.save(contract);

        contractPhase.forEach(it -> {
                it.setContract(contract);
                it.getExpenses().forEach(expense -> expense.setContractPhase(it));
            });
        contractCounterparties.forEach(it -> it.setMainContract(contract));
    }
}
