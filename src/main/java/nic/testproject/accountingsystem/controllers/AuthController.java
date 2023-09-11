package nic.testproject.accountingsystem.controllers;

import lombok.RequiredArgsConstructor;
import nic.testproject.accountingsystem.dtos.authorization.JwtResponse;
import nic.testproject.accountingsystem.dtos.authorization.LoginDTO;
import nic.testproject.accountingsystem.dtos.authorization.LoginResponseDTO;
import nic.testproject.accountingsystem.dtos.authorization.RefreshJwtRequest;
import nic.testproject.accountingsystem.dtos.authorization.RegisterDTO;
import nic.testproject.accountingsystem.dtos.authorization.RegisterResponseDTO;
import nic.testproject.accountingsystem.services.user.AuthService;
import nic.testproject.accountingsystem.services.user.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@CrossOrigin(allowCredentials = "true", originPatterns = "*")
@RestController
@RequestMapping("api/auth/")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final RegistrationService registrationService;

    @PostMapping("login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginDTO loginDTO) {
        LoginResponseDTO response = authService.login(loginDTO);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody @Valid RefreshJwtRequest request) {
        JwtResponse response = authService.refreshTokens(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("token")
    public ResponseEntity<JwtResponse> getAccessToken(RefreshJwtRequest refreshToken) {
        JwtResponse response = authService.getAccessToken(refreshToken.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody @Valid RegisterDTO registerDTO) {
        RegisterResponseDTO response = registrationService.register(registerDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

}
