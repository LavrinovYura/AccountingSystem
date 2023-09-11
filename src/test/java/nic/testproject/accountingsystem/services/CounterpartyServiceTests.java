package nic.testproject.accountingsystem.services;

import nic.testproject.accountingsystem.dtos.contracts.CounterpartyDTO;
import nic.testproject.accountingsystem.exceptions.ResourceNotFoundException;
import nic.testproject.accountingsystem.mappers.CounterpartyMapper;
import nic.testproject.accountingsystem.models.contracts.details.Counterparty;
import nic.testproject.accountingsystem.repositories.contracts.CounterpartyRepository;
import nic.testproject.accountingsystem.services.contracts.CounterpartyService;
import nic.testproject.accountingsystem.validation.CounterpartyValidation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CounterpartyServiceTests {

    @Mock
    private CounterpartyRepository counterpartyRepository;

    @Mock
    private CounterpartyMapper counterpartyMapper;

    @Mock
    private CounterpartyValidation validation;

    @InjectMocks
    private CounterpartyService counterpartyService;


    @Test
    public void testSaveCounterparty() {
        CounterpartyDTO counterpartyDTO = new CounterpartyDTO();
        Counterparty counterparty = new Counterparty();

        when(counterpartyMapper.counterpartyFromDTO(counterpartyDTO)).thenReturn(counterparty);
        doNothing().when(validation).saveValidation(counterparty);
        when(counterpartyRepository.save(counterparty)).thenReturn(counterparty);

        counterpartyService.saveCounterparty(counterpartyDTO);

        verify(counterpartyRepository, times(1)).save(counterparty);
    }

    @Test
    public void testFindCounterparties() {
        CounterpartyDTO criteria = new CounterpartyDTO();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Counterparty> counterparties = new PageImpl<>(Collections.singletonList(new Counterparty()));

        when(counterpartyRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(counterparties);
        when(counterpartyMapper.counterpartyPageToDTOSet(counterparties)).thenReturn(Collections.singleton(new CounterpartyDTO()));

        Set<CounterpartyDTO> foundCounterparties = counterpartyService.findCounterparties(criteria, pageable);

        assertFalse(foundCounterparties.isEmpty());
        verify(counterpartyRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    public void testUpdateCounterparty() {
        Long id = 1L;
        CounterpartyDTO counterpartyDTO = new CounterpartyDTO();
        Counterparty existingCounterparty = new Counterparty();
        Counterparty updatedCounterparty = new Counterparty();

        when(counterpartyRepository.findById(id)).thenReturn(Optional.of(existingCounterparty));
        doNothing().when(validation).updateValidation(existingCounterparty, counterpartyDTO);
        when(counterpartyMapper.updateCounterpartyFromDto(counterpartyDTO, existingCounterparty)).thenReturn(updatedCounterparty);

        counterpartyService.updateCounterparty(counterpartyDTO, id);

        verify(counterpartyRepository, times(1)).findById(id);
    }

    @Test
    public void testDeleteCounterparty() {
        Long id = 1L;
        Counterparty counterparty = new Counterparty();

        when(counterpartyRepository.findById(id)).thenReturn(Optional.of(counterparty));

        counterpartyService.deleteCounterparty(id);

        verify(counterpartyRepository, times(1)).delete(counterparty);
    }

    @Test
    public void testFindCounterparties_NoCounterpartiesFound() {
        CounterpartyDTO criteria = new CounterpartyDTO();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Counterparty> emptyPage = new PageImpl<>(Collections.emptyList());

        when(counterpartyRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(emptyPage);

        assertThrows(ResourceNotFoundException.class, () -> {
            counterpartyService.findCounterparties(criteria, pageable);
        });

        verify(counterpartyRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    public void testUpdateCounterparty_CounterpartyNotFound() {
        Long id = 1L;
        CounterpartyDTO counterpartyDTO = new CounterpartyDTO();

        when(counterpartyRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            counterpartyService.updateCounterparty(counterpartyDTO, id);
        });

        verify(counterpartyRepository, times(1)).findById(id);
    }

    @Test
    public void testDeleteCounterparty_CounterpartyNotFound() {
        Long id = 1L;

        when(counterpartyRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            counterpartyService.deleteCounterparty(id);
        });

        verify(counterpartyRepository, times(1)).findById(id);
    }
}
