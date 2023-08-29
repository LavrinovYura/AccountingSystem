package nic.testproject.accountingsystem.models.contracts.details;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nic.testproject.accountingsystem.models.contracts.Contract;
import nic.testproject.accountingsystem.models.contracts.ContractType;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "contracts_counterparties")
@Data
@NoArgsConstructor
public class ContractCounterparty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Please enter contract name")
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Please enter contract type")
    private ContractType type;

    @JsonFormat(pattern="yyyy-MM-dd")
    @NotNull(message = "Please enter the planned start date")
    private LocalDate plannedStartDate;

    @JsonFormat(pattern="yyyy-MM-dd")
    @NotNull(message = "Please enter the planned end date")
    private LocalDate plannedEndDate;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate actualStartDate;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate actualEndDate;

    @NotNull(message = "Please enter the amount")
    @DecimalMin("0.0")
    private Double amount;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne()
    @JoinColumn(name = "contract_id", referencedColumnName = "id")
    private Contract contract;

    @ManyToOne()
    @JoinColumn(name = "counterparty_id", referencedColumnName = "id")
    private Counterparty counterparty;
}
