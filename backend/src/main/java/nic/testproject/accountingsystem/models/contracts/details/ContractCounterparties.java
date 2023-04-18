package nic.testproject.accountingsystem.models.contracts.details;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nic.testproject.accountingsystem.models.contracts.Contract;
import nic.testproject.accountingsystem.models.contracts.ContractType;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "contracts_counterparties")
@Data
@NoArgsConstructor
public class ContractCounterparties {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ContractType type;

    private Double amount;
    private LocalDate plannedStartDate;
    private LocalDate plannedEndDate;
    private LocalDate actualStartDate;
    private LocalDate actualEndDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "counterparty_id")
    private Counterparty counterparty;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "contract_id", referencedColumnName = "id")
    private Contract contract2;


}
