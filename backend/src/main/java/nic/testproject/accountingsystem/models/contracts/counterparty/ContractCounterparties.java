package nic.testproject.accountingsystem.models.contracts.counterparty;

import lombok.Data;
import lombok.NoArgsConstructor;
import nic.testproject.accountingsystem.models.contracts.Contract;
import nic.testproject.accountingsystem.models.contracts.details.ContractType;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counterparty_id")
    private Counterparty counterparty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id")
    private Contract contract;

    private Double amount;

    private LocalDate plannedStartDate;

    private LocalDate plannedEndDate;

    private LocalDate actualStartDate;

    private LocalDate actualEndDate;

}
