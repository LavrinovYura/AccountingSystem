package nic.testproject.accountingsystem.controllers;

import nic.testproject.accountingsystem.dto.RequestName;
import nic.testproject.accountingsystem.dto.contracts.CounterpartyDTO;
import nic.testproject.accountingsystem.exceptions.ValidationException;
import nic.testproject.accountingsystem.models.contracts.details.Counterparty;
import nic.testproject.accountingsystem.repositories.contracts.CounterpartyRepository;
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
    private final CounterpartyRepository counterpartyRepository;

    @Autowired
    public CounterpartyController(CounterpartyService counterpartyService, ModelMapper modelMapper, CounterpartyRepository counterpartyRepository) {
        this.counterpartyService = counterpartyService;
        this.modelMapper = modelMapper;
        this.counterpartyRepository = counterpartyRepository;
    }

    @PostMapping("save")
    public ResponseEntity<CounterpartyDTO> saveCounterparty(
            @RequestBody CounterpartyDTO counterpartyDTO) throws ValidationException {
        if (counterpartyRepository.existsByName(counterpartyDTO.getName())) {
            return ResponseEntity.badRequest().build();
        }
        CounterpartyDTO savedCounterparty = counterpartyService.saveCounterparty(counterpartyDTO);
        return ResponseEntity.ok(savedCounterparty);
    }

    @PostMapping("show")
    public ResponseEntity<List<CounterpartyDTO>> getCounterparties(
            @RequestBody CounterpartyDTO criteria,
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

    @PutMapping("update/{name}")
    public ResponseEntity<CounterpartyDTO> updateCounterparty(
            @PathVariable String name,
            @RequestBody CounterpartyDTO counterpartyDTO) throws ValidationException {
        CounterpartyDTO counterparty = counterpartyService.updateCounterparty(counterpartyDTO,name);
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
