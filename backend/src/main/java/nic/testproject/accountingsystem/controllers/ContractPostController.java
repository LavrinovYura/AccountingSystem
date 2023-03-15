package nic.testproject.accountingsystem.controllers;

import nic.testproject.accountingsystem.dto.contracts.ContractDTO;
import nic.testproject.accountingsystem.models.contracts.Contract;
import nic.testproject.accountingsystem.repositories.contracts.ContractRepository;
import nic.testproject.accountingsystem.services.contracts.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/menu/contract")
public class ContractPostController {

    private final ContractRepository contractRepository;
    private final ContractService contractService;

    @Autowired
    public ContractPostController(ContractRepository contractRepository, ContractService contractService) {
        this.contractRepository = contractRepository;
        this.contractService = contractService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveContract(@RequestBody Contract contractDTO) {
        if (contractRepository.existsByName(contractDTO.getName())) {
            return new ResponseEntity<>("Contract already exist", HttpStatus.BAD_REQUEST);
        }
        contractService.addContract(contractDTO);
        return ResponseEntity.ok().build();
    }
}
