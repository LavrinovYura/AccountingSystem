package nic.testproject.accountingsystem.controllers;

import lombok.RequiredArgsConstructor;
import nic.testproject.accountingsystem.dtos.administration.PersonDTO;
import nic.testproject.accountingsystem.dtos.administration.RequestRole;
import nic.testproject.accountingsystem.services.AdminService;
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

import javax.validation.Valid;
import java.util.Set;

@CrossOrigin(allowCredentials = "true", originPatterns = "*")
@RestController
@RequestMapping("api/menu/administration/")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("users")
    public ResponseEntity<Set<PersonDTO>> getUsers(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "50") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Set<PersonDTO> persons = adminService.getUsers(pageable);

        return ResponseEntity.ok().body(persons);
    }

    @DeleteMapping("users/{id}/delete")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("users/{personId}/addRole")
    public ResponseEntity<Void> addRole(
            @RequestBody @Valid RequestRole requestRole,
            @PathVariable Long personId
    ) {
        adminService.addRole(requestRole.getRoleType(), personId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("users/{personId}/removeRole")
    public ResponseEntity<Void> removeRole(
            @RequestBody @Valid RequestRole requestRole,
            @PathVariable Long personId) {
        adminService.removeRole(requestRole.getRoleType(), personId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("users/usersByRole")
    public ResponseEntity<Set<PersonDTO>> getUsersByRole(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "50") int size,
            @RequestBody @Valid RequestRole requestRole
    ) {
        Pageable pageable = PageRequest.of(page, size);

        Set<PersonDTO> persons = adminService.getUsersByRole(pageable, requestRole.getRoleType());

        return ResponseEntity.ok(persons);
    }

}
