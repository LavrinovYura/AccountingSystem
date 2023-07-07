package nic.testproject.accountingsystem.models.contracts;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "Please enter the planned start date")
    private LocalDate plannedStartDate;
    @JsonFormat(pattern="yyyy-MM-dd")
    @NotNull(message = "Please enter the planned end date")
    private LocalDate plannedEndDate;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate actualStartDate;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate actualEndDate;

    @NotNull(message = "Please enter the amount")
    @DecimalMin("0.0")
    private Double amount;

    @OneToMany(mappedBy = "contract1", cascade = CascadeType.ALL)
    private List<ContractPhase> phases;

    @OneToMany(mappedBy = "contract2", cascade = CascadeType.ALL)
    private List<ContractCounterparties> contractCounterparties;
}
