package nic.testproject.accountingsystem.controllers;

import lombok.RequiredArgsConstructor;
import nic.testproject.accountingsystem.dtos.contracts.ContractCounterpartiesDTO;
import nic.testproject.accountingsystem.dtos.contracts.ContractCounterpartyDTO;
import nic.testproject.accountingsystem.dtos.contracts.ContractCriteriaDTO;
import nic.testproject.accountingsystem.dtos.contracts.ContractDTO;
import nic.testproject.accountingsystem.dtos.contracts.ContractPhaseDTO;
import nic.testproject.accountingsystem.dtos.contracts.ContractPhasesDTO;
import nic.testproject.accountingsystem.services.contracts.ContractService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Set;

@CrossOrigin
@RestController
@RequestMapping("api/menu/contracts/")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @PostMapping("save")
    public ResponseEntity<ContractDTO> saveContract(
            @RequestBody @Valid ContractDTO contractDTO) {
        ContractDTO savedContract = contractService.saveContract(contractDTO);

        return ResponseEntity.ok(savedContract);
    }

    @PostMapping("show")
    public ResponseEntity<Set<ContractDTO>> getContracts(
            @RequestBody ContractCriteriaDTO criteria,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "50") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Set<ContractDTO> contracts = contractService.getContracts(criteria, pageable);

        return ResponseEntity.ok(contracts);
    }

    @PutMapping("{id}/update")
    public ResponseEntity<ContractDTO> updateContract(
            @PathVariable Long id,
            @RequestBody ContractDTO ContractDTO) {
        @Valid ContractDTO updatedContract = contractService.updateContract(ContractDTO, id);
        return ResponseEntity.ok(updatedContract);
    }

    @PostMapping("{id}/addPhases")
    public ResponseEntity<Set<ContractPhaseDTO>> addContractPhases(
            @PathVariable Long id,
            @RequestBody @Valid ContractPhasesDTO phasesDTO) {
        Set<ContractPhaseDTO> addedPhases = contractService.addContractPhases(phasesDTO.getContractPhases(), id);
        return ResponseEntity.ok(addedPhases);
    }

    @PostMapping("{id}/addContractCounterparty")
    public ResponseEntity<Set<ContractCounterpartyDTO>> addContractCounterparty(
            @PathVariable Long id,
            @RequestBody @Valid ContractCounterpartiesDTO contractCounterpartiesDTO) {
        Set<ContractCounterpartyDTO> addedContractCounterparties = contractService
                .addContractCounterparty(contractCounterpartiesDTO.getContractCounterparties(), id);
        return ResponseEntity.ok(addedContractCounterparties);
    }

    @DeleteMapping("{id}/delete")
    public ResponseEntity<Void> deleteContract(@PathVariable Long id) {
        contractService.deleteContract(id);
        return ResponseEntity.ok().build();
    }
}
