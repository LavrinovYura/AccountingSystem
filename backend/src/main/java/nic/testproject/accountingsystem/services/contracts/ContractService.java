package nic.testproject.accountingsystem.services.contracts;

import nic.testproject.accountingsystem.dto.contracts.ContractDTO;
import nic.testproject.accountingsystem.models.contracts.Contract;
import nic.testproject.accountingsystem.repositories.contracts.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ContractService {

    private final ContractRepository contractRepository;

    @Autowired
    public ContractService(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    public ResponseEntity<String> addContract(ContractDTO contractDTO) {
        if(contractRepository.existsByName(contractDTO.getName())) {
            return new ResponseEntity<>("Contract already exist", HttpStatus.BAD_REQUEST);
        }
        contractRepository.save(
                Contract.builder()
                .name(contractDTO.getName())
                .type(contractDTO.getType())
                .actualEndDate(contractDTO.getActualEndDate())
                .actualStartDate(contractDTO.getActualStartDate())
                .amount(contractDTO.getAmount())
                .contractCounterparties(contractDTO.getContractCounterparties())
                .planedEndDate(contractDTO.getPlanedEndDate())
                .planedStartDate(contractDTO.getPlanedStartDate())
                .phases(contractDTO.getPhases())
                .build()
        );

        return new ResponseEntity<>("Contract saved", HttpStatus.OK);
    }
}
