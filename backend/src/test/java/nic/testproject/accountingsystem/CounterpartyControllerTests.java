package nic.testproject.accountingsystem;

import nic.testproject.accountingsystem.controllers.CounterpartyController;
import nic.testproject.accountingsystem.dto.RequestName;
import nic.testproject.accountingsystem.dto.contracts.CounterpartyDTO;
import nic.testproject.accountingsystem.exceptions.ResourceNotFoundException;
import nic.testproject.accountingsystem.models.contracts.details.Counterparty;
import nic.testproject.accountingsystem.repositories.contracts.CounterpartyRepository;
import nic.testproject.accountingsystem.services.contracts.CounterpartyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CounterpartyControllerTests {

    @Mock
    private CounterpartyService counterpartyService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private CounterpartyRepository counterpartyRepository;

    @InjectMocks
    private CounterpartyController counterpartyController;

    @Test
    void saveCounterparty_ValidCounterpartyDTO_ReturnsOkResponse() {
        // Arrange
        CounterpartyDTO counterpartyDTO = new CounterpartyDTO();
        counterpartyDTO.setName("Test Counterparty");

        when(counterpartyService.saveCounterparty(any(CounterpartyDTO.class)))
                .thenReturn(new Counterparty());

        when(modelMapper.map(any(), eq(CounterpartyDTO.class)))
                .thenReturn(counterpartyDTO);

        // Act
        ResponseEntity<CounterpartyDTO> response = counterpartyController.saveCounterparty(counterpartyDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(counterpartyDTO, response.getBody());
        verify(counterpartyService, times(1)).saveCounterparty(any(CounterpartyDTO.class));
    }

    @Test
    void getCounterparties_NoCounterparties_ReturnsNotFoundResponse() {
        // Arrange
        CounterpartyDTO criteria = new CounterpartyDTO();
        Pageable pageable = PageRequest.of(0, 50);
        Page<Counterparty> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        when(counterpartyService.findCounterparties(any(CounterpartyDTO.class), any(Pageable.class)))
                .thenReturn(emptyPage);

        // Act
        ResponseEntity<List<CounterpartyDTO>> response = counterpartyController.getCounterparties(criteria, 0, 50);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getCounterparties_CounterpartiesExist_ReturnsOkResponseWithCounterparties() {
        // Arrange
        CounterpartyDTO criteria = new CounterpartyDTO();
        Pageable pageable = PageRequest.of(0, 50);
        List<Counterparty> counterparties = Collections.singletonList(new Counterparty());
        Page<Counterparty> counterpartiesPage = new PageImpl<>(counterparties, pageable, counterparties.size());
        List<CounterpartyDTO> expectedCounterpartyDTOs = Collections.singletonList(new CounterpartyDTO());

        when(counterpartyService.findCounterparties(any(CounterpartyDTO.class), any(Pageable.class)))
                .thenReturn(counterpartiesPage);

        when(modelMapper.map(any(), eq(CounterpartyDTO.class)))
                .thenReturn(new CounterpartyDTO());

        // Act
        ResponseEntity<List<CounterpartyDTO>> response = counterpartyController.getCounterparties(criteria, 0, 50);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedCounterpartyDTOs, response.getBody());
    }

    @Test
    void updateCounterparty_ValidCounterpartyDTO_ReturnsOkResponseWithUpdatedCounterparty() {
        // Arrange
        String name = "Test Counterparty";
        CounterpartyDTO counterpartyDTO = new CounterpartyDTO();
        counterpartyDTO.setName(name);

        when(counterpartyService.updateCounterparty(any(CounterpartyDTO.class), eq(name)))
                .thenReturn(counterpartyDTO);

        // Act
        ResponseEntity<CounterpartyDTO> response = counterpartyController.updateCounterparty(name, counterpartyDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(counterpartyDTO, response.getBody());
        verify(counterpartyService, times(1)).updateCounterparty(any(CounterpartyDTO.class), eq(name));
    }

    @Test
    void deleteCounterparty_ValidName_ReturnsOkResponse() {
        // Arrange
        RequestName counterpartyName = new RequestName();

        // Act
        ResponseEntity<Void> response = counterpartyController.deleteCounterparty(counterpartyName);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(counterpartyService, times(1)).deleteCounterparty(counterpartyName.getName());
    }

    @Test
    void deleteCounterparty_NonExistingCounterparty_ThrowsResourceNotFoundException() {
        // Arrange
        RequestName counterpartyName = new RequestName();

        // Mock the service to throw ResourceNotFoundException
        doThrow(new ResourceNotFoundException()).when(counterpartyService)
                .deleteCounterparty(counterpartyName.getName());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            counterpartyController.deleteCounterparty(counterpartyName);
        });
        verify(counterpartyService, times(1)).deleteCounterparty(counterpartyName.getName());
    }


    @Test
    void deleteCounterpartyWithChildren_ExistingCounterparty_ReturnsOkResponse() {
        // Arrange
        RequestName counterpartyName = new RequestName();

        // Mock the repository to return an existing counterparty
        Counterparty counterparty = new Counterparty();

        // Act
        ResponseEntity<Void> response = counterpartyController.deleteCounterpartyWithChildren(counterpartyName);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(counterpartyService, times(1)).deleteCounterpartyWithChildren(counterpartyName.getName());
    }

    @Test
    void deleteCounterpartyWithChildren_NonExistingCounterparty_ThrowsResourceNotFoundException() {
        // Arrange
        RequestName counterpartyName = new RequestName();

        // Mock the service to throw ResourceNotFoundException
        doThrow(new ResourceNotFoundException()).when(counterpartyService)
                .deleteCounterpartyWithChildren(counterpartyName.getName());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
            counterpartyController.deleteCounterpartyWithChildren(counterpartyName)
        );
        verify(counterpartyService, times(1)).deleteCounterpartyWithChildren(counterpartyName.getName());
    }

}
