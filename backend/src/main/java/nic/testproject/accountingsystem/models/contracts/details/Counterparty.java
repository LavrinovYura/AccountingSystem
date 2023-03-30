package nic.testproject.accountingsystem.models.contracts.details;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "counterparties")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Counterparty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    private String inn;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "counterparty",cascade = CascadeType.ALL)
    private List<ContractCounterparties> contractCounterparties;
}
