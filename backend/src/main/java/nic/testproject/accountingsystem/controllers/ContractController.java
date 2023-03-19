package nic.testproject.accountingsystem.controllers;

import nic.testproject.accountingsystem.dto.contracts.ContractDTO;
import nic.testproject.accountingsystem.dto.contracts.update.UpdateContractDTO;
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
@RequestMapping("api/menu/contracts/")
public class ContractController {

    private final ContractService contractService;
    private final ContractRepository contractRepository;

    @Autowired
    public ContractController(ContractService contractService, ContractRepository contractRepository) {
        this.contractService = contractService;
        this.contractRepository = contractRepository;
    }

    @PostMapping("save")
    public ResponseEntity<Contract> saveContract(@RequestBody ContractDTO contractDTO) {
        if (contractRepository.existsByName(contractDTO.getName())) {
            return ResponseEntity.badRequest().build();
        }
        Contract savedContract = contractService.saveContract(contractDTO);
        return ResponseEntity.ok(savedContract);
    }

    @GetMapping("show")
    public ResponseEntity<List<Contract>> getContracts(
            @ModelAttribute ContractDTO criteria,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "50") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Contract> contractPage = contractService.getContracts(criteria, pageable);

        if (contractPage.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Contract> contracts = new ArrayList<>(contractPage.getContent());
        return ResponseEntity.ok(contracts);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<Contract> updateContract(
            @PathVariable Long id,
            @RequestBody UpdateContractDTO updateContractDTO) {
        Contract updatedContract = contractService.updateContract(id, updateContractDTO);
        return ResponseEntity.ok(updatedContract);
    }
}
