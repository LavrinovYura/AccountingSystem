package nic.testproject.accountingsystem.models.contracts;

import lombok.Data;
import lombok.NoArgsConstructor;
import nic.testproject.accountingsystem.models.contracts.details.ContractCounterparty;
import nic.testproject.accountingsystem.models.contracts.details.ContractPhase;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "contracts")
@Data
@NoArgsConstructor
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

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

    //@PrePersist
    //private void linkIds(){
    //    System.out.println(this);
    //    this.phases.forEach(it -> it.setContract(this));
    //    this.contractCounterparties.forEach(it -> it.setContract(this));
    //}
}
