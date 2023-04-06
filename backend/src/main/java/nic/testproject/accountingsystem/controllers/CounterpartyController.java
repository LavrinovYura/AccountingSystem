package nic.testproject.accountingsystem.controllers;

import nic.testproject.accountingsystem.dto.RequestName;
import nic.testproject.accountingsystem.dto.contracts.CounterpartyDTO;
import nic.testproject.accountingsystem.models.contracts.details.Counterparty;
import nic.testproject.accountingsystem.services.contracts.CounterpartyService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("api/menu/counterparties/")
public class CounterpartyController {

    private final CounterpartyService counterpartyService;
    private final ModelMapper modelMapper;

    @Autowired
    public CounterpartyController(CounterpartyService counterpartyService, ModelMapper modelMapper) {
        this.counterpartyService = counterpartyService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("show")
    public ResponseEntity<List<CounterpartyDTO>> getCounterparties(
            @ModelAttribute CounterpartyDTO criteria,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "50") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Counterparty> counterpartiesPage = counterpartyService.findCounterparties(criteria, pageable);

        if (counterpartiesPage.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<CounterpartyDTO> contracts = counterpartiesPage.getContent().stream()
                .map(counterparty -> modelMapper.map(counterparty, CounterpartyDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(contracts);
    }

    @PutMapping("update")
    public ResponseEntity<CounterpartyDTO> updateCounterparty(@RequestBody CounterpartyDTO counterpartyDTO) {
        CounterpartyDTO counterparty = modelMapper.map(counterpartyService.updateCounterparty(counterpartyDTO),CounterpartyDTO.class);
        return ResponseEntity.ok(counterparty);
    }

    @DeleteMapping("delete")
    public ResponseEntity<Void> deleteCounterparty(@RequestBody RequestName name) {
        counterpartyService.deleteCounterparty(name.getName());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("deleteAll")
    public ResponseEntity<Void> deleteCounterpartyWithChildren(@RequestBody RequestName name) {
        counterpartyService.deleteCounterpartyWithChildren(name.getName());
        return ResponseEntity.ok().build();
    }
}
