package nic.testproject.accountingsystem.controllers;

import lombok.RequiredArgsConstructor;
import nic.testproject.accountingsystem.dtos.contracts.CounterpartyDTO;
import nic.testproject.accountingsystem.services.contracts.CounterpartyService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
@RequestMapping("api/menu/counterparties/")
@RequiredArgsConstructor
public class CounterpartyController {

    private final CounterpartyService counterpartyService;

    @PostMapping("save")
    public ResponseEntity<CounterpartyDTO> saveCounterparty(
            @RequestBody @Valid CounterpartyDTO counterpartyDTO) {

        CounterpartyDTO savedCounterparty = counterpartyService.saveCounterparty(counterpartyDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedCounterparty);
    }

    @PostMapping("show")
    public ResponseEntity<Set<CounterpartyDTO>> getCounterparties(
            @RequestBody CounterpartyDTO criteria,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "50") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Set<CounterpartyDTO> counterparties= counterpartyService.findCounterparties(criteria, pageable);

        return ResponseEntity.ok(counterparties);
    }

    @PutMapping("{id}/update")
    public ResponseEntity<CounterpartyDTO> updateCounterparty(
            @PathVariable Long id,
            @RequestBody @Valid CounterpartyDTO counterpartyDTO)  {
        CounterpartyDTO counterparty = counterpartyService.updateCounterparty(counterpartyDTO, id);
        return ResponseEntity.ok(counterparty);
    }

    @DeleteMapping("{id}/delete")
    public ResponseEntity<Void> deleteCounterparty(@PathVariable Long id) {
        counterpartyService.deleteCounterparty(id);
        return ResponseEntity.ok().build();
    }
}
