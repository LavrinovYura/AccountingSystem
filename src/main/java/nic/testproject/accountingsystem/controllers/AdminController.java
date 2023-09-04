package nic.testproject.accountingsystem.controllers;

import lombok.RequiredArgsConstructor;
import nic.testproject.accountingsystem.dtos.administration.RequestRole;
import nic.testproject.accountingsystem.dtos.administration.PersonDTO;
import nic.testproject.accountingsystem.models.user.Person;
import nic.testproject.accountingsystem.services.AdminService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("api/menu/administration/")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("users")
    public ResponseEntity<Set<PersonDTO>> getUsers(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "50") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Set<PersonDTO> persons = adminService.getUsers(pageable);

        return ResponseEntity.ok(persons);
    }

    @DeleteMapping("users/{id}/delete")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("users/addRole")
    public ResponseEntity<?> addRole(
            @RequestBody RequestRole requestRole) {
        adminService.addRole(requestRole.getRoleType(), requestRole.getId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("users/removeRole")
    public ResponseEntity<?> removeRole(
            @RequestBody RequestRole requestRole) {
        adminService.removeRole(requestRole.getRoleType(), requestRole.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("users/usersByRole")
    public ResponseEntity<Set<PersonDTO>> getUsersByRole(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "50") int size,
            @RequestBody RequestRole requestRole) {
        Pageable pageable = PageRequest.of(page, size);

        Set<PersonDTO> persons = adminService.getUsersByRole(pageable, requestRole.getRoleType());

        return ResponseEntity.ok(persons);
    }

}
