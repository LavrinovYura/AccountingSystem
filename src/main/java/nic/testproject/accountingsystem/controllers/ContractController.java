package nic.testproject.accountingsystem.controllers;

import lombok.RequiredArgsConstructor;
import nic.testproject.accountingsystem.dtos.contracts.ContractCounterpartiesDTO;
import nic.testproject.accountingsystem.dtos.contracts.ContractCounterpartyDTO;
import nic.testproject.accountingsystem.dtos.contracts.ContractCriteriaDTO;
import nic.testproject.accountingsystem.dtos.contracts.ContractDTO;
import nic.testproject.accountingsystem.dtos.contracts.ContractPhaseDTO;
import nic.testproject.accountingsystem.dtos.contracts.ContractPhasesDTO;
import nic.testproject.accountingsystem.dtos.contracts.UpdateContractDTO;
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

@CrossOrigin(allowCredentials = "true", originPatterns = "*")
@RestController
@RequestMapping("api/menu/contracts/")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @PostMapping("saveContract")
    public ResponseEntity<ContractDTO> saveContract(
            @RequestBody @Valid ContractDTO contractDTO) {
        ContractDTO savedContract = contractService.saveContract(contractDTO);

        return ResponseEntity.ok(savedContract);
    }

    @PostMapping("showContracts")
    public ResponseEntity<Set<ContractDTO>> getContracts(
            @RequestBody ContractCriteriaDTO criteria,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "50") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Set<ContractDTO> contracts = contractService.getContracts(criteria, pageable);

        return ResponseEntity.ok(contracts);
    }

    @PutMapping("{contractId}/updateContract")
    public ResponseEntity<ContractDTO> updateContract(
            @PathVariable Long contractId,
            @RequestBody @Valid UpdateContractDTO contract ) {
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

    @PostMapping("{contractId}/addContractCounterparties")
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

    @DeleteMapping("{contractId}/deleteContract")
    public ResponseEntity<Void> deleteContract(@PathVariable Long contractId) {
        contractService.deleteContract(contractId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{contractCounterpartyId}/deleteContractCounterparty")
    public ResponseEntity<Void> deleteContractCounterparty(@PathVariable Long contractCounterpartyId) {
        contractService.deleteContractCounterparty(contractCounterpartyId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{phaseId}/deletePhase")
    public ResponseEntity<Void> deletePhase(@PathVariable Long phaseId) {
        contractService.deletePhase(phaseId);
        return ResponseEntity.ok().build();
    }
}
