package nic.testproject.accountingsystem.models.contracts.details;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "counterparties")
@Data
@NoArgsConstructor
public class Counterparty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;

    @Column(unique = true)
    private String inn;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "counterparty")
    private List<ContractCounterparty> contractCounterparties;

    //ToDo а можно ли вообще удалять контрагента ? И что делать с контрактом с этим контрагентом если я удаляю его контрагента ?
    @PreRemove
    private void unlinkContractCounterparties(){
        this.getContractCounterparties().forEach(it -> it.setCounterparty(null));
        this.setContractCounterparties(null);
    }
}
