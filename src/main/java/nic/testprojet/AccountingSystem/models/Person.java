package nic.testprojet.AccountingSystem.models;


import javax.validation.constraints.Size;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @NonNull
    @NotEmpty(message = "Имя не должно быть пустым")
    @Size(min = 5, max = 100, message = "ФИО должно быть от 5 до 100 символов")
    private String fullName;

    @NonNull
    @UniqueElements
    private String username;

    private String password;

    @NonNull
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles = new ArrayList<>();

    private String expireDate;

}
