package nic.testproject.accountingsystem;

import nic.testproject.accountingsystem.controllers.AuthController;
import nic.testproject.accountingsystem.dto.authorization.LoginDTO;
import nic.testproject.accountingsystem.dto.authorization.LoginResponseDTO;
import nic.testproject.accountingsystem.dto.authorization.RegisterDTO;
import nic.testproject.accountingsystem.dto.authorization.RegisterResponseDTO;
import nic.testproject.accountingsystem.models.user.Person;
import nic.testproject.accountingsystem.services.security.JWT.JWTGenerator;
import nic.testproject.accountingsystem.services.user.LoginService;
import nic.testproject.accountingsystem.services.user.RegistrationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTGenerator jwtGenerator;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private LoginService loginService;

    @Mock
    private RegistrationService registrationService;

    @InjectMocks
    private AuthController authController;

    @Test
    public void testRegister() {
        // Arrange
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("testuser");
        registerDTO.setPassword("testpassword");
        registerDTO.setFirstName("John");
        registerDTO.setSecondName("Doe");
        registerDTO.setMiddleName("Smith");

        Person registeredPerson = new Person();
        registeredPerson.setFirstName("John");
        registeredPerson.setSecondName("Doe");
        registeredPerson.setMiddleName("Smith");

        RegisterResponseDTO expectedResponse = new RegisterResponseDTO();
        expectedResponse.setFirstName("John");
        expectedResponse.setSecondName("Doe");
        expectedResponse.setMiddleName("Smith");

        when(registrationService.register(registerDTO)).thenReturn(registeredPerson);
        when(modelMapper.map(registeredPerson, RegisterResponseDTO.class)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<RegisterResponseDTO> response = authController.register(registerDTO);

        // Assert
        verify(registrationService, times(1)).register(registerDTO);
        verify(modelMapper, times(1)).map(registeredPerson, RegisterResponseDTO.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void testLogin() {
        // Arrange
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testuser");
        loginDTO.setPassword("testpassword");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        Person person = new Person();
        person.setFirstName("John");
        person.setSecondName("Doe");
        person.setMiddleName("Smith");
        when(loginService.findPersonByUsername(loginDTO.getUsername())).thenReturn(person);

        String token = "testtoken";
        when(jwtGenerator.generateToken(authentication)).thenReturn(token);

        LoginResponseDTO expectedResponse = new LoginResponseDTO();
        expectedResponse.setAccessToken(token);
        expectedResponse.setFirstName(person.getFirstName());
        expectedResponse.setSecondName(person.getSecondName());
        expectedResponse.setMiddleName(person.getMiddleName());

        // Act
        ResponseEntity<LoginResponseDTO> response = authController.login(loginDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(loginService, times(1)).findPersonByUsername(loginDTO.getUsername());
        verify(jwtGenerator, times(1)).generateToken(authentication);
        verifyNoMoreInteractions(authenticationManager, loginService, jwtGenerator, modelMapper);
    }
}
