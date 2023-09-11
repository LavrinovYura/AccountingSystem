package nic.testproject.accountingsystem.services.reports;

import lombok.RequiredArgsConstructor;
import nic.testproject.accountingsystem.dtos.report.AllContracts;
import nic.testproject.accountingsystem.exceptions.ConflictException;
import nic.testproject.accountingsystem.exceptions.ResourceNotFoundException;
import nic.testproject.accountingsystem.repositories.contracts.ContractCounterpartyRepository;
import nic.testproject.accountingsystem.repositories.contracts.ContractRepository;
import nic.testproject.accountingsystem.repositories.contracts.projections.ContractCounterpartiesProjection;
import nic.testproject.accountingsystem.repositories.contracts.projections.ContractPhaseProjection;
import nic.testproject.accountingsystem.repositories.contracts.projections.ContractProjection;
import nic.testproject.accountingsystem.repositories.contracts.projections.ContractWithPhasesProjection;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.apache.poi.ss.usermodel.BorderStyle.MEDIUM;
import static org.apache.poi.ss.usermodel.BorderStyle.THIN;

@Service
@RequiredArgsConstructor
public class ContractReportService {

    private final ContractRepository contractRepository;
    private final ContractCounterpartyRepository contractCounterpartyRepository;

    public ByteArrayOutputStream generateContractsExcelReport(AllContracts contracts) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            List<ContractProjection> contractsMain = contracts.getContracts();
            List<ContractCounterpartiesProjection> contractsCounterparties = contracts.getContractCounterparties();

            contractsCounterparties.forEach(it -> contractsMain.forEach(contract -> {
                if (!contract.getName().equals(it.getContract().getName())){
                    contractsMain.add(it.getContract());
                }
            }));

            Sheet sheet = workbook.createSheet("Contracts");
            // создаем стили для ячеек заголовка
            CellStyle headerStyle = createHeaderStyle(workbook);
            // создаем стили для ячеек со значениями
            CellStyle cellStyle = createCellStyle(workbook);

            int rowNum = 0;
            Row headerRow = sheet.createRow(rowNum++);
            headerRow.createCell(0).setCellValue("#");
            headerRow.createCell(1).setCellValue("Договор");
            headerRow.createCell(2).setCellValue("Название");
            headerRow.createCell(3).setCellValue("Тип");
            headerRow.createCell(4).setCellValue("Плановые сроки начала");
            headerRow.createCell(5).setCellValue("Плановые сроки окончания");
            headerRow.createCell(6).setCellValue("Фактические сроки начала");
            headerRow.createCell(7).setCellValue("Фактические сроки окончания");
            headerRow.createCell(8).setCellValue("Сумма");

            for (ContractCounterpartiesProjection contractCounterparties : contractsCounterparties) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(rowNum - 1);
                row.createCell(1).setCellValue("Контрагент");
                row.createCell(2).setCellValue(contractCounterparties.getName());
                row.createCell(3).setCellValue(contractCounterparties.getType().name());
                row.createCell(4).setCellValue(contractCounterparties.getPlannedStartDate().toString());
                row.createCell(5).setCellValue(contractCounterparties.getPlannedEndDate().toString());
                row.createCell(6).setCellValue(contractCounterparties.getActualStartDate() != null ?
                        contractCounterparties.getActualStartDate().toString() : "");
                row.createCell(7).setCellValue(contractCounterparties.getActualEndDate() != null ?
                        contractCounterparties.getActualEndDate().toString() : "");
                row.createCell(8).setCellValue(contractCounterparties.getAmount());

                // применяем стили к ячейкам со значениями
                for (int i = 0; i < row.getLastCellNum(); i++) {
                    row.getCell(i).setCellStyle(cellStyle);
                }
            }

            for (ContractProjection contract : contractsMain) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(rowNum - 1);
                row.createCell(1).setCellValue("Основной");
                row.createCell(2).setCellValue(contract.getName());
                row.createCell(3).setCellValue(contract.getType().name());
                row.createCell(4).setCellValue(contract.getPlannedStartDate().toString());
                row.createCell(5).setCellValue(contract.getPlannedEndDate().toString());
                row.createCell(6).setCellValue(contract.getActualStartDate() != null ?
                        contract.getActualStartDate().toString() : "");
                row.createCell(7).setCellValue(contract.getActualEndDate() != null ?
                        contract.getActualEndDate().toString() : "");
                row.createCell(8).setCellValue(contract.getAmount());

                // применяем стили к ячейкам со значениями
                for (int i = 0; i < row.getLastCellNum(); i++) {
                    row.getCell(i).setCellStyle(cellStyle);
                }
            }

            // применяем стили к ячейкам заголовка
            return writeBook(workbook, sheet, headerStyle, headerRow);
        }
    }

    public ByteArrayOutputStream generatePhasesExcelReport(List<ContractPhaseProjection> contractPhases) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Contract Phases");
            // создаем стили для ячеек заголовка
            CellStyle headerStyle = createHeaderStyle(workbook);
            // создаем стили для ячеек со значениями
            CellStyle cellStyle = createCellStyle(workbook);

            int rowNum = 0;
            Row headerRow = sheet.createRow(rowNum++);
            headerRow.createCell(0).setCellValue("#");
            headerRow.createCell(1).setCellValue("Название");
            headerRow.createCell(2).setCellValue("Плановые сроки начала");
            headerRow.createCell(3).setCellValue("Плановые сроки окончания");
            headerRow.createCell(4).setCellValue("Фактические\nсроки начала");
            headerRow.createCell(5).setCellValue("Фактические\nсроки окончания");
            headerRow.createCell(6).setCellValue("Сумма этапа");
            headerRow.createCell(7).setCellValue("Плановые траты\nна материалы");
            headerRow.createCell(8).setCellValue("Фактические траты\nна материалы");
            headerRow.createCell(9).setCellValue("Плановые траты\nна зарплату");
            headerRow.createCell(10).setCellValue("Фактические траты\nна зарплату");

            for (ContractPhaseProjection contractPhase : contractPhases) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(rowNum - 1);
                row.createCell(1).setCellValue(contractPhase.getName());
                row.createCell(2).setCellValue(contractPhase.getPlannedStartDate().toString());
                row.createCell(3).setCellValue(contractPhase.getPlannedEndDate().toString());
                row.createCell(4).setCellValue(contractPhase.getActualStartDate() != null ? contractPhase.getActualStartDate().toString() : "");
                row.createCell(5).setCellValue(contractPhase.getActualEndDate() != null ? contractPhase.getActualEndDate().toString() : "");
                row.createCell(6).setCellValue(contractPhase.getPhaseCost());
                row.createCell(7).setCellValue(contractPhase.getPlannedMaterialCosts() != null ? contractPhase.getPlannedMaterialCosts().toString() : "");
                row.createCell(8).setCellValue(contractPhase.getActualMaterialCosts() != null ? contractPhase.getActualMaterialCosts().toString() : "");
                row.createCell(9).setCellValue(contractPhase.getPlannedSalaryExpenses() != null ? contractPhase.getPlannedSalaryExpenses().toString() : "");
                row.createCell(10).setCellValue(contractPhase.getActualSalaryExpenses() != null ? contractPhase.getActualSalaryExpenses().toString() : "");

                // применяем стили к ячейкам со значениями
                for (int i = 0; i < row.getLastCellNum(); i++) {
                    row.getCell(i).setCellStyle(cellStyle);
                }
            }
            // применяем стили к ячейкам заголовка
            return writeBook(workbook, sheet, headerStyle, headerRow);
        }
    }

    private ByteArrayOutputStream writeBook(Workbook workbook, Sheet sheet, CellStyle headerStyle, Row headerRow) throws IOException {
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            headerRow.getCell(i).setCellStyle(headerStyle);
            // настройка авто-размера ячеек
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);

        return out;
    }
    private CellStyle createCellStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setBorderBottom(THIN);
        cellStyle.setBorderLeft(MEDIUM);
        cellStyle.setBorderRight(MEDIUM);
        return cellStyle;
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setWrapText(true);
        headerStyle.setBorderTop(MEDIUM);
        headerStyle.setBorderBottom(MEDIUM);
        headerStyle.setBorderLeft(MEDIUM);
        headerStyle.setBorderRight(MEDIUM);
        return headerStyle;
    }

    public AllContracts getAllContractsByPeriod(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new ConflictException("End date can't be before start date");
        }

        List<ContractProjection> contracts = contractRepository
                .findByPlannedStartDateBetween(startDate, endDate);
        List<ContractCounterpartiesProjection> contractCounterparties = contractCounterpartyRepository
                .findByPlannedStartDateBetween(startDate, endDate);

        if (contracts.isEmpty() || contractCounterparties.isEmpty()) {
            throw new ResourceNotFoundException("There is no contracts in this period");
        }

        return new AllContracts(contracts, contractCounterparties);
    }

    public List<ContractPhaseProjection> getAllPhasesByContract(Long id) {
        ContractWithPhasesProjection contract = contractRepository.findContractById(id)
                .orElseThrow(() -> new ResourceNotFoundException("There is no contract with id " + id));
        return contract.getPhases();
    }

}
