package nic.testproject.accountingsystem.dto.contracts;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ContractPhaseDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Please enter contract name")
    private String name;

    @JsonFormat(pattern="yyyy-MM-dd")
    @NotNull(message = "Please enter the planned start date")
    private LocalDate plannedStartDate;

    @JsonFormat(pattern="yyyy-MM-dd")
    @NotNull(message = "Please enter the planned end date")
    private LocalDate plannedEndDate;

    @NotNull(message = "Please enter the planned costs of materials")
    @DecimalMin("0.0")
    private BigDecimal plannedMaterialCosts;

    @NotNull(message = "Please enter the planned expenditures for salaries")
    @DecimalMin("0.0")
    private BigDecimal phaseCost;

    @NotNull(message = "Please enter the planned expenses for the phase")
    @DecimalMin("0.0")
    private BigDecimal plannedSalaryExpenses;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate actualStartDate;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate actualEndDate;

    @DecimalMin("0.0")
    private BigDecimal actualMaterialCosts;

    @DecimalMin("0.0")
    private BigDecimal actualSalaryExpenses;
}
