package nic.testproject.accountingsystem.models.contracts;

import lombok.*;
import nic.testproject.accountingsystem.models.contracts.counterparty.ContractCounterparties;
import nic.testproject.accountingsystem.models.contracts.details.ContractPhase;
import nic.testproject.accountingsystem.models.contracts.details.ContractType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "contracts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Enumerated(EnumType.STRING)
    private ContractType type;

    private LocalDate plannedStartDate;
    private LocalDate plannedEndDate;
    private LocalDate actualStartDate;
    private LocalDate actualEndDate;

    private Double amount;

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL)
    private List<ContractPhase> phases;

    @OneToMany(mappedBy = "mainContract", cascade = CascadeType.ALL)
    private List<ContractCounterparties> contractCounterparties;
}
