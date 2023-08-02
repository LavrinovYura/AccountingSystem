package nic.testproject.accountingsystem.models.contracts.details;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "counterparties")
@Data
@NoArgsConstructor
public class Counterparty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Please enter name of counterparty")
    private String name;

    @NotBlank(message = "Please enter address of counterparty")
    private String address;

    @NotBlank(message = "Please enter INN of counterparty")
    private String inn;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "counterparty",cascade = CascadeType.ALL)
    private List<ContractCounterparties> contractCounterparties;
}
