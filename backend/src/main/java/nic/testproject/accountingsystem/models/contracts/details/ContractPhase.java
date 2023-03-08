package nic.testproject.accountingsystem.models.contracts.details;

import lombok.Data;
import lombok.NoArgsConstructor;
import nic.testproject.accountingsystem.models.contracts.Contract;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;


@Entity
@Table(name = "contract_phases")
@Data
@NoArgsConstructor
public class ContractPhase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    private LocalDate plannedStartDate;

    private LocalDate plannedEndDate;

    private LocalDate actualStartDate;

    private LocalDate actualEndDate;

    private Double phaseCost;

    @OneToMany(mappedBy = "contractPhase", cascade = CascadeType.ALL)
    private Set<PhaseExpense> expenses;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id")
    private Contract contract;

    public void addExpense(PhaseExpense expense) {
        expenses.add(expense);
        expense.setContractPhase(this);
    }
}
