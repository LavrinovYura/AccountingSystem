package nic.testproject.accountingsystem.models.contracts;

import lombok.*;
import nic.testproject.accountingsystem.models.contracts.details.ContractCounterparties;
import nic.testproject.accountingsystem.models.contracts.details.ContractPhase;
import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "contracts")
@Data
@NoArgsConstructor
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Please enter contract name")
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Please enter contract type")
    private ContractType type;

    @NotNull(message = "Please enter the planned start date")
    private LocalDate plannedStartDate;
    @NotNull(message = "Please enter the planned end date")
    private LocalDate plannedEndDate;

    private LocalDate actualStartDate;
    private LocalDate actualEndDate;

    @NotNull(message = "Please enter the amount")
    @DecimalMin("0.0")
    private Double amount;

    @OneToMany(mappedBy = "contract1", cascade = CascadeType.ALL)
    private List<ContractPhase> phases;

    @OneToMany(mappedBy = "contract2", cascade = CascadeType.ALL)
    private List<ContractCounterparties> contractCounterparties;
}
