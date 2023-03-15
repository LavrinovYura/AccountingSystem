package nic.testproject.accountingsystem.models.contracts.counterparty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
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
    private String inn;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "counterparty", cascade = CascadeType.ALL)
    private List<ContractCounterparties> contractCounterparties;
}
