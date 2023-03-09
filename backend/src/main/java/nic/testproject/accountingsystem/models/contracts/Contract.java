package nic.testproject.accountingsystem.models.contracts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nic.testproject.accountingsystem.models.contracts.counterparty.ContractCounterparties;
import nic.testproject.accountingsystem.models.contracts.details.ContractPhase;
import nic.testproject.accountingsystem.models.contracts.details.ContractType;

import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "contracts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ContractType type;

    private LocalDate planedStartDate;

    private LocalDate planedEndDate;

    private LocalDate actualStartDate;

    private LocalDate actualEndDate;

    private Double amount;

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL)
    private List<ContractPhase> phases;

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL)
    private List<ContractCounterparties> contractCounterparties;

    //Переместить в Contract service ? или вообще не нужно, пускай пока будет
    @AssertTrue(message = "Actual start date cannot be after actual end date")
    private boolean isActualDatesValid() {
        if (actualStartDate == null || actualEndDate == null) {
            return true;
        }
        return !actualStartDate.isAfter(actualEndDate);
    }

    @AssertTrue(message = "Actual start date cannot be after actual end date")
    private boolean isPlanedDatesValid() {
        if (actualStartDate == null || actualEndDate == null) {
            return true;
        }
        return !actualStartDate.isAfter(actualEndDate);
    }
}
