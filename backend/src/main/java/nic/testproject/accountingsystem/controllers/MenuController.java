package nic.testproject.accountingsystem.controllers;

import nic.testproject.accountingsystem.dto.contracts.ContractDTO;
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
@RequestMapping("api/menu")
public class MenuController {

    private final ContractRepository contractRepository;
    private final ContractService contractService;

    @Autowired
    public MenuController(ContractRepository contractRepository, ContractRepository contractRepository1, ContractService contractService) {
        this.contractRepository = contractRepository1;
        this.contractService = contractService;
    }

    @PostMapping("contract/save")
    public ResponseEntity<String> saveContract(@RequestBody ContractDTO contractDTO){

    return  contractService.addContract(contractDTO);

    }

}
