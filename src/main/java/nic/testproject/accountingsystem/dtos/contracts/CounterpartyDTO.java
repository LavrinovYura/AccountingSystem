package nic.testproject.accountingsystem.dtos.contracts;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CounterpartyDTO {

    private Long id;

    @NotBlank(message = "Please enter name of counterparty")
    private String name;

    @NotBlank(message = "Please enter address of counterparty")
    private String address;

    @NotBlank(message = "Please enter INN of counterparty")
    private String inn;
}
