package nic.testproject.accountingsystem.controllers;

import nic.testproject.accountingsystem.dto.RequestName;
import nic.testproject.accountingsystem.dto.administration.RequestRole;
import nic.testproject.accountingsystem.dto.administration.UserDTO;
import nic.testproject.accountingsystem.models.user.Person;
import nic.testproject.accountingsystem.services.AdminService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("api/menu/administration/")
public class AdminController {

    private final AdminService adminService;
    private final ModelMapper modelMapper;

    @Autowired
    public AdminController(AdminService adminService, ModelMapper modelMapper) {
        this.adminService = adminService;
        this.modelMapper = modelMapper;
    }

    //Поменять на RequestBody
    @GetMapping("users")
    public ResponseEntity<List<UserDTO>> getUsers(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "50") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Person> personPage = adminService.getUsers(pageable);

        if (personPage.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<UserDTO> users = personPage.getContent().stream()
                .map(contract -> modelMapper.map(contract, UserDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    @DeleteMapping("users/delete")
    public ResponseEntity<Void> deleteUser(@RequestBody RequestName name) {
        adminService.deleteUser(name.getName());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("users/addRole")
    public ResponseEntity<UserDTO> addRole(
            @RequestBody RequestRole requestRole) {
        adminService.addRole(requestRole.getRoleType(), requestRole.getName());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("users/removeRole")
    public ResponseEntity<UserDTO> removeRole(
            @RequestBody RequestRole requestRole) {
        adminService.removeRole(requestRole.getRoleType(), requestRole.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("users/usersByRole")
    public ResponseEntity<List<UserDTO>> getUsersByRole(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "50") int size,
            @RequestBody RequestRole requestRole) {
        Pageable pageable = PageRequest.of(page, size);
        List<Person> personPage = adminService.getUsersByRole(pageable, requestRole.getRoleType());

        if (personPage.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<UserDTO> users = personPage.stream()
                .map(contract -> modelMapper.map(contract, UserDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

}
