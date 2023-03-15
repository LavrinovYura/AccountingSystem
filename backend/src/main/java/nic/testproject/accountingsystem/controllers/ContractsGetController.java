package nic.testproject.accountingsystem.controllers;

import nic.testproject.accountingsystem.dto.contracts.ContractDTO;
import nic.testproject.accountingsystem.models.contracts.Contract;
import nic.testproject.accountingsystem.services.contracts.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/menu/")
public class ContractsGetController {

    private final ContractService contractService;

    @Autowired
    public ContractsGetController(ContractService contractService) {
        this.contractService = contractService;
    }

    @GetMapping("contracts")
    public ResponseEntity<List<Contract>> getContracts(
            @ModelAttribute ContractDTO criteria,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Contract> contractPage = contractService.findContracts(criteria, pageable);

        if (contractPage.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Contract> contracts = new ArrayList<>(contractPage.getContent());
        return ResponseEntity.ok(contracts);
    }
}
