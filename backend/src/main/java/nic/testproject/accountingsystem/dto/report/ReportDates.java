package nic.testproject.accountingsystem.dto.report;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReportDates {
    private LocalDate plannedStartDate;
    private LocalDate plannedEndDate;
}
