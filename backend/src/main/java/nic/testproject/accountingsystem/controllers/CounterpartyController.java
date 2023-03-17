package nic.testproject.accountingsystem.controllers;

import nic.testproject.accountingsystem.dto.contracts.CounterpartyDTO;
import nic.testproject.accountingsystem.models.contracts.Contract;
import nic.testproject.accountingsystem.models.contracts.counterparty.Counterparty;
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
@RequestMapping("api/menu/")
public class CounterpartyController {

    private final CounterpartyService counterpartyService;

    @Autowired
    public CounterpartyController(CounterpartyService counterpartyService) {
        this.counterpartyService = counterpartyService;
    }

    @GetMapping("counterparties")
    public ResponseEntity<List<Counterparty>> getContracts(
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
}
