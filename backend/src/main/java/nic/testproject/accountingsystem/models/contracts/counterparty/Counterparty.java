package nic.testproject.accountingsystem.models.contracts.counterparty;

import lombok.Data;
import lombok.NoArgsConstructor;

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

    @OneToMany(mappedBy = "counterparty", cascade = CascadeType.ALL)
    private List<ContractCounterparties> contractCounterparties;
}
