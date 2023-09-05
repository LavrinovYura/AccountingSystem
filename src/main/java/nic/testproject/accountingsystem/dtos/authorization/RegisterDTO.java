package nic.testproject.accountingsystem.dtos.authorization;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class RegisterDTO {
    @NotBlank(message = "Please enter your name")
    @NotNull
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    @Pattern(regexp = "^[A-Za-zА-Юа-ю]+$", message = "Name must consist of only letters")
    private String firstName;

    @NotBlank(message = "Please enter your last name")
    @NotNull
    @Size(min = 1, max = 100, message = "Last name must be between 1 and 100 characters")
    @Pattern(regexp = "^[A-Za-zА-Юа-ю]+$", message = "Last name must consist of only letters")
    private String secondName;

    @NotBlank(message = "Please enter your middle name")
    @NotNull
    @Size(min = 5, max = 100, message = "Middle name must be between 5 and 100 characters")
    @Pattern(regexp = "^[A-Za-zА-Юа-ю]+$", message = "Middle name must consist of only letters")
    private String middleName;

    @NotNull
    private String username;

    @NotNull
    private String password;
}
