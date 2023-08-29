package accountingsystem.services;

import nic.testproject.accountingsystem.dto.contracts.CounterpartyDTO;
import nic.testproject.accountingsystem.exceptions.ConflictException;
import nic.testproject.accountingsystem.exceptions.ResourceNotFoundException;
import nic.testproject.accountingsystem.exceptions.ValidationException;
import nic.testproject.accountingsystem.models.contracts.details.ContractCounterparty;
import nic.testproject.accountingsystem.models.contracts.details.Counterparty;
import nic.testproject.accountingsystem.repositories.contracts.CounterpartyRepository;
import nic.testproject.accountingsystem.services.contracts.CounterpartyService;
import nic.testproject.accountingsystem.validation.CounterpartyValidation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CounterpartyServiceTests {

    @Mock
    private CounterpartyRepository counterpartyRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private CounterpartyValidation validation;

    @InjectMocks
    private CounterpartyService counterpartyService;


    @Test
    void saveCounterparty_ValidData_CallsSaveMethod() {
        // Создаем тестовые данные
        CounterpartyDTO counterpartyDTO = new CounterpartyDTO();
        // Задаем значения свойств counterpartyDTO

        Counterparty counterparty = new Counterparty();
        // Задаем значения свойств counterparty

        // Мокируем необходимые методы и их возвращаемые значения
        when(modelMapper.map(counterpartyDTO, Counterparty.class)).thenReturn(counterparty);
        when(modelMapper.map(counterparty, CounterpartyDTO.class)).thenReturn(counterpartyDTO);
        when(counterpartyRepository.save(counterparty)).thenReturn(counterparty);

        // Вызываем метод saveCounterparty
        CounterpartyDTO result = counterpartyService.saveCounterparty(counterpartyDTO);

        assertEquals(counterpartyDTO, result);
        // Проверяем, что метод save был вызван один раз с объектом counterparty
        verify(counterpartyRepository, times(1)).save(counterparty);
    }

    @Test
    void saveCounterparty_ThrowsConflictException() throws ConflictException {
        // Создаем тестовые данные
        CounterpartyDTO counterpartyDTO = new CounterpartyDTO();
        // Задаем значения свойств counterpartyDTO
        Counterparty counterparty = new Counterparty();
        // Задаем значения свойств counterparty
        BindingResult errors = new BeanPropertyBindingResult(counterparty, "counterparty");

        // Мокируем необходимые методы и их возвращаемые значения
        when(modelMapper.map(counterpartyDTO, Counterparty.class)).thenReturn(counterparty);
        doThrow(ConflictException.class).when(validation).saveValidation(counterparty);

        // Вызываем метод saveCounterparty и проверяем, что он вызывает исключение ConflictException
        assertThrows(ConflictException.class, () -> counterpartyService.saveCounterparty(counterpartyDTO));
    }

    @Test
    void findCounterparties_ReturnsPageOfCounterparties() {
        // Создаем тестовые данные
        CounterpartyDTO criteria = new CounterpartyDTO();
        Pageable pageable = Pageable.ofSize(10).withPage(0);

        // Создаем список контрагентов, которые будут возвращены из репозитория
        List<Counterparty> counterparties = new ArrayList<>();
        counterparties.add(new Counterparty());
        counterparties.add(new Counterparty());
        counterparties.add(new Counterparty());

        // Создаем объект Page с контрагентами
        Page<Counterparty> page = new PageImpl<>(counterparties, pageable, counterparties.size());

        // Мокируем необходимые методы и их возвращаемые значения
        when(counterpartyRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        // Вызываем метод findCounterparties
        Page<Counterparty> result = counterpartyService.findCounterparties(criteria, pageable);

        // Проверяем, что метод findAll был вызван один раз с соответствующими аргументами
        verify(counterpartyRepository, times(1)).findAll(any(Specification.class), eq(pageable));

        // Проверяем, что результат содержит ожидаемое количество контрагентов
        assertEquals(3, result.getTotalElements());
    }


    @Test
    void updateCounterparty_ValidData_CallsSaveMethod() {
        // Создаем тестовые данные
        CounterpartyDTO counterpartyDTO = new CounterpartyDTO();
        // Задаем значения свойств counterpartyDTO

        String name = "ExistingName";

        Optional<Counterparty> optionalCounterparty = Optional.of(new Counterparty());
        Counterparty counterparty = optionalCounterparty.get();

        // Мокируем необходимые методы и их возвращаемые значения
        when(counterpartyRepository.findByName(name)).thenReturn(optionalCounterparty);
        doNothing().when(modelMapper).map(any(CounterpartyDTO.class), any(Counterparty.class));
        when(counterpartyRepository.save(counterparty)).thenReturn(counterparty);
        when(modelMapper.map(counterparty, CounterpartyDTO.class)).thenReturn(counterpartyDTO);
        // Вызываем метод updateCounterparty
        CounterpartyDTO result = counterpartyService.updateCounterparty(counterpartyDTO, name);

        assertEquals(result,counterpartyDTO);
        // Проверяем, что метод findByName был вызван один раз с заданным именем
        verify(counterpartyRepository, times(1)).findByName(name);
        // Проверяем, что метод save был вызван один раз с объектом counterparty
        verify(counterpartyRepository, times(1)).save(counterparty);
        // Проверяем, что метод map был вызван один раз с объектами counterpartyDTO и Counterparty
        verify(modelMapper, times(1)).map(counterparty, CounterpartyDTO.class);
    }

    @Test
    void updateCounterparty_ThrowsResourceNotFoundException() {
        // Создаем тестовые данные
        CounterpartyDTO counterpartyDTO = new CounterpartyDTO();
        // Задаем значения свойств counterpartyDTO

        String name = "NonExistingName";

        // Мокируем необходимые методы и их возвращаемые значения
        when(counterpartyRepository.findByName(name)).thenReturn(Optional.empty());

        // Вызываем метод updateCounterparty и проверяем, что он вызывает исключение ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class, () -> counterpartyService.updateCounterparty(counterpartyDTO, name));

        // Проверяем, что метод findByName был вызван один раз с заданным именем
        verify(counterpartyRepository, times(1)).findByName(name);
    }

    @Test
    void updateCounterparty_ThrowsValidationException() throws ConflictException {
        // Создаем тестовые данные
        CounterpartyDTO counterpartyDTO = new CounterpartyDTO();
        // Задаем значения свойств counterpartyDTO

        String name = "ExistingName";

        Optional<Counterparty> optionalCounterparty = Optional.of(new Counterparty());
        Counterparty counterparty = optionalCounterparty.get();

        // Мокируем необходимые методы и их возвращаемые значения
        when(counterpartyRepository.findByName(name)).thenReturn(optionalCounterparty);

        doThrow(ValidationException.class).when(validation).updateValidation(counterparty, counterpartyDTO);
        // Вызываем метод updateCounterparty и проверяем, что он вызывает исключение ConflictException
        assertThrows(ValidationException.class, () -> counterpartyService.updateCounterparty(counterpartyDTO, name));
        // Проверяем, что метод findByName был вызван один раз с заданным именем
        verify(counterpartyRepository, times(1)).findByName(name);
    }

    @Test
    void deleteCounterparty_ExistingCounterparty_DeletesCounterparty() {
        // Arrange
        String name = "ExistingName";
        Counterparty existingCounterparty = new Counterparty();
        List<ContractCounterparty> counterparties = new ArrayList<>();
        existingCounterparty.setName(name);
        existingCounterparty.setContractCounterparties(counterparties);
        Optional<Counterparty> optionalCounterparty = Optional.of(existingCounterparty);

        when(counterpartyRepository.findByName(name)).thenReturn(optionalCounterparty);

        // Act
        counterpartyService.deleteCounterparty(name);

        // Assert
        verify(counterpartyRepository, times(1)).delete(existingCounterparty);
    }

    @Test
    void deleteCounterparty_NonExistingCounterparty_ThrowsResourceNotFoundException() {
        // Arrange
        String name = "NonExistingName";
        Optional<Counterparty> optionalCounterparty = Optional.empty();

        when(counterpartyRepository.findByName(name)).thenReturn(optionalCounterparty);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> counterpartyService.deleteCounterparty(name));
    }

    @Test
    void deleteCounterpartyWithChildren_ExistingCounterparty_DeletesCounterparty() {
        // Arrange
        String name = "ExistingName";
        Counterparty existingCounterparty = new Counterparty();
        existingCounterparty.setName(name);
        Optional<Counterparty> optionalCounterparty = Optional.of(existingCounterparty);

        when(counterpartyRepository.findByName(name)).thenReturn(optionalCounterparty);

        // Act
        counterpartyService.deleteCounterpartyWithChildren(name);

        // Assert
        verify(counterpartyRepository, times(1)).delete(existingCounterparty);
    }

    @Test
    void deleteCounterpartyWithChildren_NonExistingCounterparty_ThrowsException() {
        // Arrange
        String name = "NonExistingName";
        Optional<Counterparty> optionalCounterparty = Optional.empty();

        when(counterpartyRepository.findByName(name)).thenReturn(optionalCounterparty);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> counterpartyService.deleteCounterpartyWithChildren(name));
    }
}
