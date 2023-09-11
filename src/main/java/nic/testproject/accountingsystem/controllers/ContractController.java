package nic.testproject.accountingsystem.controllers;

import lombok.RequiredArgsConstructor;
import nic.testproject.accountingsystem.dtos.contracts.*;
import nic.testproject.accountingsystem.services.contracts.ContractService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("{contractId}/update")
    public ResponseEntity<ContractDTO> updateContract(
            @PathVariable Long contractId,
            @RequestBody AbstractContract contract ) {
        @Valid ContractDTO updatedContract = contractService.updateContract(contract, contractId);
        return ResponseEntity.ok(updatedContract);
    }

    @PostMapping("{contractId}/addPhases")
    public ResponseEntity<Set<ContractPhaseDTO>> addContractPhases(
            @PathVariable Long contractId,
            @RequestBody @Valid ContractPhasesDTO phasesDTO) {
        Set<ContractPhaseDTO> addedPhases = contractService
                .addContractPhases(phasesDTO.getContractPhases(), contractId);
        return ResponseEntity.ok(addedPhases);
    }

    @PostMapping("{contractId}/addContractCounterparty")
    public ResponseEntity<Set<ContractCounterpartyDTO>> addContractCounterparty(
            @PathVariable Long contractId,
            @RequestBody @Valid ContractCounterpartiesDTO contractCounterpartiesDTO) {
        Set<ContractCounterpartyDTO> addedContractCounterparties = contractService
                .addContractCounterparty(contractCounterpartiesDTO.getContractCounterparties(), contractId);
        return ResponseEntity.ok(addedContractCounterparties);
    }

    @PostMapping("{phaseId}/updatePhase")
    public ResponseEntity<ContractPhaseDTO> updateContractPhase(
            @PathVariable Long phaseId,
            @RequestBody @Valid ContractPhaseDTO phaseDTO) {
        ContractPhaseDTO updatedPhase = contractService.updateContractPhase(phaseDTO, phaseId);
        return ResponseEntity.ok(updatedPhase);
    }

    @PostMapping("{contractCounterpartyId}/updateContractCounterparty")
    public ResponseEntity<ContractCounterpartyDTO> updateContractCounterparty(
            @PathVariable Long contractCounterpartyId,
            @RequestBody @Valid ContractCounterpartyDTO contractCounterpartyDTO) {
        ContractCounterpartyDTO updatedContractCounterparty = contractService
                .updateContractCounterparty(contractCounterpartyDTO, contractCounterpartyId);
        return ResponseEntity.ok(updatedContractCounterparty);
    }

    @DeleteMapping("{contractId}/delete")
    public ResponseEntity<Void> deleteContract(@PathVariable Long contractId) {
        contractService.deleteContract(contractId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{contractCounterpartyId}/delete")
    public ResponseEntity<Void> deleteContractCounterparty(@PathVariable Long contractCounterpartyId) {
        contractService.deleteContractCounterparty(contractCounterpartyId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{phaseId}/delete")
    public ResponseEntity<Void> deletePhase(@PathVariable Long phaseId) {
        contractService.deletePhase(phaseId);
        return ResponseEntity.ok().build();
    }
}
