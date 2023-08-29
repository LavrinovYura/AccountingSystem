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
@RequestMapping("api/menu/counterparties/")
public class CounterpartyController {

    private final CounterpartyService counterpartyService;
    private final ModelMapper modelMapper;

    @Autowired
    public CounterpartyController(CounterpartyService counterpartyService, ModelMapper modelMapper) {
        this.counterpartyService = counterpartyService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("save")
    public ResponseEntity<CounterpartyDTO> saveCounterparty(
            @RequestBody CounterpartyDTO counterpartyDTO) {

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

        List<CounterpartyDTO> contracts = counterpartiesPage.getContent().stream()
                .map(counterparty -> modelMapper.map(counterparty, CounterpartyDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(contracts);
    }

    @PutMapping("{id}/update")
    public ResponseEntity<CounterpartyDTO> updateCounterparty(
            @PathVariable Long id,
            @RequestBody CounterpartyDTO counterpartyDTO)  {
        CounterpartyDTO counterparty = counterpartyService.updateCounterparty(counterpartyDTO, id);
        return ResponseEntity.ok(counterparty);
    }

    @DeleteMapping("{id}/delete")
    public ResponseEntity<Void> deleteCounterparty(@PathVariable Long id) {
        counterpartyService.deleteCounterparty(id);
        return ResponseEntity.ok().build();
    }
}
