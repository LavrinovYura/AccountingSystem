package nic.testproject.accountingsystem.controllers;

import nic.testproject.accountingsystem.dto.RequestName;
import nic.testproject.accountingsystem.dto.administration.UserDTO;
import nic.testproject.accountingsystem.dto.authorization.LoginDTO;
import nic.testproject.accountingsystem.models.user.Person;
import nic.testproject.accountingsystem.models.user.RoleType;
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

    @PutMapping("users/addrole")
    public ResponseEntity<UserDTO> addRole(
            @RequestBody RoleType roleType,
            @RequestBody RequestName name) {
        adminService.addRole(roleType,name.getName());
        return ResponseEntity.noContent().build();
    }

}
