package nic.testproject.accountingsystem.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import nic.testproject.accountingsystem.dtos.authorization.JwtResponse;
import nic.testproject.accountingsystem.dtos.authorization.LoginDTO;
import nic.testproject.accountingsystem.dtos.authorization.LoginResponseDTO;
import nic.testproject.accountingsystem.dtos.authorization.RefreshJwtRequest;
import nic.testproject.accountingsystem.dtos.authorization.RegisterDTO;
import nic.testproject.accountingsystem.dtos.authorization.RegisterResponseDTO;
import nic.testproject.accountingsystem.services.user.AuthService;
import nic.testproject.accountingsystem.services.user.RegistrationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private RegistrationService registrationService;


    @Test
    @WithMockUser(authorities = {"USER"}, username = "admin")
    public void testLogin_returnsOk() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setPassword("password");
        loginDTO.setUsername("username");

        when(authService.login(any())).thenReturn(new LoginResponseDTO());

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginDTO))
                                .with(csrf()))
                .andExpect(status().isOk());
        verify(authService, times(1)).login(any());
    }

    @Test
    @WithMockUser(authorities = {"USER"}, username = "admin")
    public void testGetNewRefreshToken_ReturnsOk() throws Exception {
        RefreshJwtRequest request = new RefreshJwtRequest();

        JwtResponse jwtResponse = new JwtResponse("access","refresh");

        when(authService.refreshTokens(any())).thenReturn(jwtResponse);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/auth/refresh")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.refreshToken").value("refresh"))
                .andExpect(jsonPath("$.accessToken").value("access"));

        verify(authService, times(1)).refreshTokens(request.getRefreshToken());
    }

    @Test
    @WithMockUser(authorities = {"USER"}, username = "admin")
    public void testGetAccessToken_ReturnsOk() throws Exception {
        RefreshJwtRequest request = new RefreshJwtRequest();

        JwtResponse jwtResponse = new JwtResponse("access","refresh");

        when(authService.getAccessToken(any())).thenReturn(jwtResponse);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/auth/token")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access"));

        verify(authService, times(1)).getAccessToken(request.getRefreshToken());
    }

    @Test
    @WithMockUser(authorities = {"USER"}, username = "admin")
    public void testRegister_ReturnsCreated() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setFirstName("John");
        registerDTO.setSecondName("Doe");
        registerDTO.setMiddleName("Smith");
        registerDTO.setUsername("johndoe");
        registerDTO.setPassword("secretpassword");


        RegisterResponseDTO responseDTO = new RegisterResponseDTO();
        responseDTO.setFirstName("John");
        responseDTO.setSecondName("Doe");
        responseDTO.setMiddleName("Smith");

        when(registrationService.register(any())).thenReturn(responseDTO);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registerDTO))
                                .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"));

        verify(registrationService, times(1)).register(registerDTO);
    }

}
