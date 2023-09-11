package nic.testproject.accountingsystem.dtos.report;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RequestDates {
    private LocalDate plannedStartDate;
    private LocalDate plannedEndDate;
}
