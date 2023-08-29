package nic.testproject.accountingsystem.controllers;

import lombok.RequiredArgsConstructor;
import nic.testproject.accountingsystem.dto.RequestName;
import nic.testproject.accountingsystem.dto.contracts.ContractCriteriaDTO;
import nic.testproject.accountingsystem.dto.contracts.ContractDTO;
import nic.testproject.accountingsystem.models.contracts.Contract;
import nic.testproject.accountingsystem.services.contracts.ContractService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
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

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("api/menu/contracts/")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;
    private final ModelMapper modelMapper;

    @PostMapping("save")
    public ResponseEntity<ContractDTO> saveContract(
            @RequestBody ContractDTO contractDTO) {
        ContractDTO savedContract = contractService.saveContract(contractDTO);

        return ResponseEntity.ok(savedContract);
    }

    @PostMapping("show")
    public ResponseEntity<List<ContractDTO>> getContracts(
            @RequestBody ContractCriteriaDTO criteria,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "50") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Contract> contractPage = contractService.getContracts(criteria, pageable);

        List<ContractDTO> contracts = contractPage.getContent().stream()
                .map(contract -> modelMapper.map(contract, ContractDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(contracts);
    }

    @PutMapping("update/{name}")
    public ResponseEntity<ContractDTO> updateContract(
            @PathVariable String name,
            @RequestBody ContractDTO ContractDTO) {
        ContractDTO updatedContract = contractService.updateContract(ContractDTO, name);
        return ResponseEntity.ok(updatedContract);
    }


    @DeleteMapping("delete")
    public ResponseEntity<Void> deleteContract(@RequestBody RequestName name) {
        contractService.deleteContract(name.getName());
        return ResponseEntity.ok().build();
    }
}
