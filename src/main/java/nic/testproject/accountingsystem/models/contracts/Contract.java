package nic.testproject.accountingsystem.models.contracts;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nic.testproject.accountingsystem.models.contracts.details.ContractCounterparty;
import nic.testproject.accountingsystem.models.contracts.details.ContractPhase;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "contracts")
@Getter
@Setter
@NoArgsConstructor
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    private ContractType type;

    private LocalDate plannedStartDate;
    private LocalDate plannedEndDate;
    private LocalDate actualStartDate;
    private LocalDate actualEndDate;
    private Double amount;

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, targetEntity = ContractPhase.class, fetch = FetchType.EAGER)
    private Set<ContractPhase> phases = new HashSet<>();

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, targetEntity = ContractCounterparty.class, fetch = FetchType.EAGER)
    private Set<ContractCounterparty> contractCounterparties = new HashSet<>();
}
