package nic.testproject.accountingsystem.models.user;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Please enter your name")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    @Pattern(regexp = "^[A-Za-zА-Юа-ю]+$", message = "Name must consist of only letters")
    private String firstName;

    @NotBlank(message = "Please enter your last name")
    @Size(min = 1, max = 100, message = "Last name must be between 1 and 100 characters")
    @Pattern(regexp = "^[A-Za-zА-Юа-ю]+$", message = "Last name must consist of only letters")
    private String secondName;

    @NotBlank(message = "Please enter your middle name")
    @NotNull
    @Size(min = 5, max = 100, message = "Middle name must be between 5 and 100 characters")
    @Pattern(regexp = "^[A-Za-zА-Юа-ю]+$", message = "Middle name must consist of only letters")
    private String middleName;

    @Column(unique=true)
    private String username;

    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();

    private Date expireDate;

}
