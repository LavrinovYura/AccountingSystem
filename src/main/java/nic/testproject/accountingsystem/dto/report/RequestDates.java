package nic.testproject.accountingsystem.dto.report;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RequestDates {
    private LocalDate plannedStartDate;
    private LocalDate plannedEndDate;
}
