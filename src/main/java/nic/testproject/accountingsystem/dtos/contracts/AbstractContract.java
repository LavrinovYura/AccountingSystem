package nic.testproject.accountingsystem.dtos.contracts;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import nic.testproject.accountingsystem.models.contracts.ContractType;

import javax.persistence.Enumerated;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public abstract class AbstractContract {

    @NotBlank(message = "Please enter contract name")
    private String name;

    @Enumerated
    @NotNull(message = "Please enter contract type")
    private ContractType type;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
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
    private BigDecimal amount;
}
