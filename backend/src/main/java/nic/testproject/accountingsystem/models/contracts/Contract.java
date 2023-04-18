package nic.testproject.accountingsystem.models.contracts;

import lombok.Data;
import lombok.NoArgsConstructor;
import nic.testproject.accountingsystem.models.contracts.details.ContractCounterparties;
import nic.testproject.accountingsystem.models.contracts.details.ContractPhase;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "contracts"
)
@Data
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

    @OneToMany(mappedBy = "contract1", cascade = CascadeType.ALL)
    private List<ContractPhase> phases;

    @OneToMany(mappedBy = "contract2", cascade = CascadeType.ALL)
    private List<ContractCounterparties> contractCounterparties;
}
