package nic.testproject.accountingsystem.controllers;

import nic.testproject.accountingsystem.dto.contracts.ContractDTO;
import nic.testproject.accountingsystem.models.contracts.Contract;
import nic.testproject.accountingsystem.repositories.contracts.ContractRepository;
import nic.testproject.accountingsystem.services.contracts.ContractService;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/menu")
public class MenuController {

    private final ContractRepository contractRepository;
    private final ContractService contractService;

    @Autowired
    public MenuController(ContractRepository contractRepository, ContractService contractService) {
        this.contractRepository = contractRepository;
        this.contractService = contractService;
    }

    @PostMapping("contract/save")
    public ResponseEntity<String> saveContract(@RequestBody ContractDTO contractDTO) {
        if (contractRepository.existsByName(contractDTO.getName())) {
            return new ResponseEntity<>("Contract already exist", HttpStatus.BAD_REQUEST);
        }
        return contractService.addContract(contractDTO);
    }

    @GetMapping("/contracts/{startId}-{endId}")
    public ResponseEntity<List<Contract>> getContractsInRange(@PathVariable Long startId, @PathVariable Long endId) throws Exception {
        List<Contract> contracts = contractRepository.findByIdBetween(startId,endId);
        return new ResponseEntity<>(contracts,HttpStatus.OK);
    }

    @GetMapping("/contract/{id}")
    public Contract getContractById(@PathVariable Long id) throws Exception {
        Contract contract= contractRepository.getContractById(id).get();
       return contract;
    }
}
