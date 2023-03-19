package nic.testproject.accountingsystem.services.reports;

import nic.testproject.accountingsystem.exceptions.ResourceNotFoundException;
import nic.testproject.accountingsystem.models.contracts.Contract;
import nic.testproject.accountingsystem.repositories.contracts.ContractRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ContractReportService {

    private final ContractRepository contractRepository;

    @Autowired
    public ContractReportService(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    public ByteArrayInputStream generateExcelReport(List<Contract> contracts) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Contracts");

            // создаем стили для ячеек заголовка
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // создаем стили для ячеек со значениями
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.CENTER);

            int rowNum = 0;
            Row headerRow = sheet.createRow(rowNum++);
            headerRow.createCell(0).setCellValue("#");
            headerRow.createCell(1).setCellValue("Name");
            headerRow.createCell(2).setCellValue("Type");
            headerRow.createCell(3).setCellValue("Planned Start Date");
            headerRow.createCell(4).setCellValue("Planned End Date");
            headerRow.createCell(5).setCellValue("Actual Start Date");
            headerRow.createCell(6).setCellValue("Actual End Date");
            headerRow.createCell(7).setCellValue("Amount");

            // применяем стили к ячейкам заголовка
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                headerRow.getCell(i).setCellStyle(headerStyle);
            }

            for (Contract contract : contracts) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(rowNum - 1);
                row.createCell(1).setCellValue(contract.getName());
                row.createCell(2).setCellValue(contract.getType().name());
                row.createCell(3).setCellValue(contract.getPlannedStartDate().toString());
                row.createCell(4).setCellValue(contract.getPlannedEndDate().toString());
                row.createCell(5).setCellValue(contract.getActualStartDate() != null ? contract.getActualStartDate().toString() : "");
                row.createCell(6).setCellValue(contract.getActualEndDate() != null ? contract.getActualEndDate().toString() : "");
                row.createCell(7).setCellValue(contract.getAmount());

                // применяем стили к ячейкам со значениями
                for (int i = 0; i < row.getLastCellNum(); i++) {
                    row.getCell(i).setCellStyle(cellStyle);
                }
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);

            return new ByteArrayInputStream(out.toByteArray());
        }
    }


    public List<Contract> getContractsByPeriod(LocalDate startDate, LocalDate endDate) {
        Optional<List<Contract>> optionalContract = contractRepository.findByPlannedStartDateBetween(startDate, endDate);
        if (!optionalContract.isPresent()) {
            throw new ResourceNotFoundException("Contracts not found with");
        }
        return optionalContract.get();
    }
}
