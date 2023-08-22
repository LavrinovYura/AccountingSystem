package nic.testproject.accountingsystem.controllers;

import nic.testproject.accountingsystem.dto.authorization.*;
import nic.testproject.accountingsystem.services.user.AuthService;
import nic.testproject.accountingsystem.services.user.RegistrationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("api/auth/")
public class AuthController {
    private final ModelMapper modelMapper;
    private final AuthService authService;
    private final RegistrationService registrationService;

    @Autowired
    public AuthController(ModelMapper modelMapper,
                          AuthService personService, RegistrationService registrationService) {
        this.modelMapper = modelMapper;
        this.authService = personService;
        this.registrationService = registrationService;
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        LoginResponseDTO response = authService.login(loginDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
        JwtResponse response = authService.refreshTokens(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("token")
    public ResponseEntity<JwtResponse> getAccessToken(RefreshJwtRequest refreshToken) {
        JwtResponse response = authService.getAccessToken(refreshToken.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterDTO registerDTO) {
        RegisterResponseDTO response = modelMapper
                .map(registrationService.register(registerDTO), RegisterResponseDTO.class);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

}
