//package accountingsystem.controllers;
//
//import nic.testproject.accountingsystem.controllers.ContractController;
//import nic.testproject.accountingsystem.dtos.contracts.ContractCriteriaDTO;
//import nic.testproject.accountingsystem.dtos.contracts.ContractDTO;
//import nic.testproject.accountingsystem.exceptions.ResourceNotFoundException;
//import nic.testproject.accountingsystem.models.contracts.Contract;
//import nic.testproject.accountingsystem.services.contracts.ContractService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.modelmapper.ModelMapper;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.Collections;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class ContractControllerTests {
//
//    @Mock
//    private ContractService contractService;
//
//    @Mock
//    private ModelMapper modelMapper;
//
//    @InjectMocks
//    private ContractController contractController;
//
//    @Test
//    void testSaveContract() {
//        ContractDTO contractDTO = new ContractDTO();
//        ContractDTO savedContractDTO = new ContractDTO();
//        when(contractService.saveContract(contractDTO)).thenReturn(savedContractDTO);
//
//        ResponseEntity<ContractDTO> response = contractController.saveContract(contractDTO);
//
//        assertEquals(savedContractDTO, response.getBody());
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        verify(contractService, times(1)).saveContract(contractDTO);
//    }
//
//    @Test
//    void testGetContracts() {
//        ContractCriteriaDTO criteria = new ContractCriteriaDTO();
//        criteria.setName("test"); // Установите значение для поля name
//        int page = 0;
//        int size = 50;
//        Pageable pageable = PageRequest.of(page, size);
//        doThrow(ResourceNotFoundException.class).when(contractService).getContracts(criteria, pageable);
//
//        assertThrows(ResourceNotFoundException.class, () -> contractController.getContracts(criteria, page, size));
//        verify(contractService, times(1)).getContracts(criteria, pageable);
//    }
//
//
//    @Test
//    void testGetContractsNotEmpty() {
//        ContractCriteriaDTO criteria = new ContractCriteriaDTO();
//        int page = 0;
//        int size = 50;
//        Pageable pageable = PageRequest.of(page, size);
//        Contract contract = new Contract();
//        contract.setId(1L);
//        List<Contract> contractList = Collections.singletonList(contract);
//        Page<Contract> contractPage = new PageImpl<>(contractList);
//        when(contractService.getContracts(criteria, pageable)).thenReturn(contractPage);
//
//        ResponseEntity<List<ContractDTO>> response = contractController.getContracts(criteria, page, size);
//
//        assertFalse(response.getBody().isEmpty());
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        verify(contractService, times(1)).getContracts(criteria, pageable);
//    }
//
////    @Test
////    void testUpdateContract() {
////        String name = "contract_name";
////        ContractDTO contractDTO = new ContractDTO();
////        ContractDTO updatedContractDTO = new ContractDTO();
////        when(contractService.updateContract(contractDTO, name)).thenReturn(updatedContractDTO);
////
////        ResponseEntity<ContractDTO> response = contractController.updateContract(name, contractDTO);
////
////        assertEquals(updatedContractDTO, response.getBody());
////        assertEquals(HttpStatus.OK, response.getStatusCode());
////        verify(contractService, times(1)).updateContract(contractDTO, name);
////    }
//
////    @Test
////    void testDeleteContractWithChildren() {
////        RequestName requestName = new RequestName();
////        doNothing().when(contractService).deleteContractWithChildren(requestName.getName());
////
////        ResponseEntity<Void> response = contractController.deleteContractWithChildren(requestName);
////
////        assertEquals(HttpStatus.OK, response.getStatusCode());
////        verify(contractService, times(1)).deleteContractWithChildren(requestName.getName());
////    }
//
//    @Test
//    void testDeleteContract() {
//        RequestName requestName = new RequestName();
//        doNothing().when(contractService).deleteContract(requestName.getName());
//
//        ResponseEntity<Void> response = contractController.deleteContract(requestName);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        verify(contractService, times(1)).deleteContract(requestName.getName());
//    }
//}
