package nic.testproject.accountingsystem.controllers;

import nic.testproject.accountingsystem.dto.contracts.ContractDTO;
import nic.testproject.accountingsystem.models.contracts.Contract;
import nic.testproject.accountingsystem.repositories.contracts.ContractRepository;
import nic.testproject.accountingsystem.services.contracts.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/menu/")
public class ContractController {

    private final ContractService contractService;
    private final ContractRepository contractRepository;

    @Autowired
    public ContractController(ContractService contractService, ContractRepository contractRepository) {
        this.contractService = contractService;
        this.contractRepository = contractRepository;
    }

    @PostMapping("contract/save")
    public ResponseEntity<String> saveContract(@RequestBody ContractDTO contractDTO) {
        if (contractRepository.existsByName(contractDTO.getName())) {
            return new ResponseEntity<>("Contract already exist", HttpStatus.BAD_REQUEST);
        }
        contractService.saveContract(contractDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("contracts")
    public ResponseEntity<List<Contract>> getContracts(
            @ModelAttribute ContractDTO criteria,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "50") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Contract> contractPage = contractService.findContracts(criteria, pageable);

        if (contractPage.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Contract> contracts = new ArrayList<>(contractPage.getContent());
        return ResponseEntity.ok(contracts);
    }
}
