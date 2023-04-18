package nic.testproject.accountingsystem.models.contracts.details;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nic.testproject.accountingsystem.models.contracts.Contract;
import nic.testproject.accountingsystem.models.contracts.ContractType;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "contracts_counterparties")
@Data
@NoArgsConstructor
public class ContractCounterparties {
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

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "contract_id", referencedColumnName = "id")
    private Contract contract2;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "counterparty_id")
    private Counterparty counterparty;
}
