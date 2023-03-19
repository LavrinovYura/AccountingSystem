package nic.testproject.accountingsystem.controllers;

import nic.testproject.accountingsystem.dto.contracts.ContractDTO;
import nic.testproject.accountingsystem.dto.contracts.CounterpartyDTO;
import nic.testproject.accountingsystem.dto.contracts.update.UpdateContractDTO;
import nic.testproject.accountingsystem.dto.contracts.update.UpdateCounterpartyDTO;
import nic.testproject.accountingsystem.models.contracts.details.Counterparty;
import nic.testproject.accountingsystem.services.contracts.CounterpartyService;
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
@RequestMapping("api/menu/counterparties/")
public class CounterpartyController {

    private final CounterpartyService counterpartyService;

    @Autowired
    public CounterpartyController(CounterpartyService counterpartyService) {
        this.counterpartyService = counterpartyService;
    }

    @GetMapping("show")
    public ResponseEntity<List<Counterparty>> getCounterparties(
            @ModelAttribute CounterpartyDTO criteria,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "50") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Counterparty> contractPage = counterpartyService.findCounterparties(criteria, pageable);

        if (contractPage.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Counterparty> contracts = new ArrayList<>(contractPage.getContent());
        return ResponseEntity.ok(contracts);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<Counterparty> updateContract(
             @PathVariable Long id,
             @RequestBody UpdateCounterpartyDTO updateCounterpartyDTO) {
        Counterparty counterparty = counterpartyService.updateCounterparty(id, updateCounterpartyDTO);
        return ResponseEntity.ok(counterparty);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteCounterparty(@PathVariable Long id) {
        counterpartyService.deleteCounterparty(id);
        return ResponseEntity.ok().build();
    }
}
