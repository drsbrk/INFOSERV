package bi.gov.obr.informationServices.utils;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import bi.gov.obr.informationServices.dto.ReportOneForm;
import bi.gov.obr.informationServices.dto.ReportOneProjection;
import bi.gov.obr.informationServices.dto.ReportTwoDTO;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ReportOneExcelExporter {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private CellStyle headerStyle;
    private CellStyle libelleTableauStyle;
    private CellStyle dataLineStyle;
    private CellStyle dataLineStyleCentered;
    private CellStyle subTotalStyle;
    private CellStyle totalStyle;

    private List<ReportOneProjection> reportOneProjections = new ArrayList<>();

    public ReportOneExcelExporter() {
        this.workbook = new XSSFWorkbook();
        buildStyle();
    }

    private void buildStyle() {

        headerStyle = workbook.createCellStyle();

        headerStyle.setBorderLeft(BorderStyle.THICK);
        headerStyle.setBorderBottom(BorderStyle.THICK);
        headerStyle.setBorderRight(BorderStyle.THICK);
        headerStyle.setBorderTop(BorderStyle.THICK);
        setCellStyleAllBorders(headerStyle);

        XSSFFont fontHeaderStyle = workbook.createFont();
        fontHeaderStyle.setBold(true);
        fontHeaderStyle.setFontHeight(17);
        headerStyle.setFont(fontHeaderStyle);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        setCellStyleAllBorders(headerStyle);

        libelleTableauStyle = workbook.createCellStyle();
        XSSFFont libelleTableauStyleFont = workbook.createFont();
        libelleTableauStyleFont.setBold(true);
        libelleTableauStyleFont.setFontHeight(16);
        libelleTableauStyle.setFont(libelleTableauStyleFont);
        setCellStyleAllBorders(libelleTableauStyle);

        dataLineStyle = workbook.createCellStyle();
        XSSFFont dataLineStyleFont = workbook.createFont();
        dataLineStyleFont.setBold(false);
        dataLineStyleFont.setFontHeight(15);
        dataLineStyle.setFont(dataLineStyleFont);
        dataLineStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
        setCellStyleAllBorders(dataLineStyle);

        dataLineStyleCentered = workbook.createCellStyle();
        XSSFFont dataLineStyleCenteredFont = workbook.createFont();
        dataLineStyleCenteredFont.setBold(false);
        dataLineStyleFont.setFontHeight(15);
        dataLineStyleCentered.setFont(dataLineStyleFont);
        dataLineStyleCentered.setAlignment(HorizontalAlignment.CENTER);
        dataLineStyleCentered.setVerticalAlignment(VerticalAlignment.CENTER);
        setCellStyleAllBorders(dataLineStyleCentered);

        subTotalStyle = workbook.createCellStyle();
        XSSFFont subTotalStyleFont = workbook.createFont();
        subTotalStyleFont.setBold(false);
        subTotalStyleFont.setFontHeight(16);
        subTotalStyle.setFont(subTotalStyleFont);

        totalStyle = workbook.createCellStyle();
        XSSFFont totalStyleFont = workbook.createFont();
        totalStyleFont.setBold(true);
        totalStyleFont.setFontHeight(16);
        totalStyle.setFont(totalStyleFont);
        setCellStyleAllBorders(totalStyle);
    }

    public void writeHeaderLine(ReportOneForm reportOneForm) {
        sheet = workbook.createSheet("Situation Journaliére");
        sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 5));
        Row row = sheet.createRow(0);
        createCell(row, 0, "SITUATION JOURNALIERE RF/RNF/RD AU " + reportOneForm.getSelectedDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), headerStyle);
    }

    public void writeHeaderLineTwoReport(ReportTwoDTO reportTwoDTO) {
        sheet = workbook.createSheet("Situation Journaliére");
        sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 5));
        Row row = sheet.createRow(0);
        createCell(row, 0, "JOURNAL DES RECETTES COLLECE DU "
                + reportTwoDTO.getStartingDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " AU " + reportTwoDTO.getEndingDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), headerStyle);
    }

    public void writeSummary() {

        Map<String, Long> totalParDeviseJMoinsUn =
                reportOneProjections.stream().collect(Collectors
                        .groupingBy(rep -> rep.getCurrency()
                                , Collectors.summingLong(repOne -> repOne.getMontantRecetteJMoinsUn())));

        Map<String, Long> totalParDeviseJMoinsDeux =
                reportOneProjections.stream().collect(Collectors
                        .groupingBy(rep -> rep.getCurrency()
                                , Collectors.summingLong(repOne -> repOne.getMontantRecetteJMoinsDeux())));


        int rowCount = 4;
        Set<String> devises = new HashSet<>();
        devises.addAll(totalParDeviseJMoinsUn.keySet());
        devises.addAll(totalParDeviseJMoinsDeux.keySet());


        Row rowDeviseLibelle = sheet.createRow(rowCount);
        createCell(rowDeviseLibelle, 0, " ", totalStyle);
        int deviseCount = 1;
        for (String devise : devises) {
            createCell(rowDeviseLibelle, deviseCount, devise, totalStyle);
            deviseCount++;
        }

        rowCount++;


        Row rowDeviseAmountJ1 = sheet.createRow(rowCount);
        createCell(rowDeviseAmountJ1, 0, "J-1", totalStyle);
        int deviseAmountJ1 = 1;
        for (String devise : devises) {
            createCell(rowDeviseAmountJ1, deviseAmountJ1, totalParDeviseJMoinsUn.containsKey(devise) ? totalParDeviseJMoinsUn.get(devise) : 0L, dataLineStyle);
            deviseAmountJ1++;
        }

        rowCount++;


        Row rowDeviseAmountJ2 = sheet.createRow(rowCount);
        createCell(rowDeviseAmountJ2, 0, "J-2", totalStyle);
        int deviseAmountJ2 = 1;
        for (String devise : devises) {
            createCell(rowDeviseAmountJ2, deviseAmountJ2, totalParDeviseJMoinsDeux.containsKey(devise) ? totalParDeviseJMoinsDeux.get(devise) : 0L, dataLineStyle);
            deviseAmountJ2++;
        }
        rowCount++;

        Row rowDeviseAmountTotal = sheet.createRow(rowCount);
        createCell(rowDeviseAmountTotal, 0, "Variation", totalStyle);
        int deviseAmountVariation = 1;
        for (String devise : devises) {
            Long j1 = totalParDeviseJMoinsUn.containsKey(devise) ? totalParDeviseJMoinsUn.get(devise) : 0L;
            Long j2 = totalParDeviseJMoinsDeux.containsKey(devise) ? totalParDeviseJMoinsDeux.get(devise) : 0L;
            Long variation = j1 - j2;
            createCell(rowDeviseAmountTotal, deviseAmountVariation, variation, dataLineStyle);
            deviseAmountVariation++;
        }


    }

    public void writeSummaryForReportTwo() {

        Map<String, Long> totalParDeviseJMoinsUn =
                reportOneProjections.stream().collect(Collectors
                        .groupingBy(rep -> rep.getCurrency()
                                , Collectors.summingLong(repOne -> repOne.getMontantRecetteCollecte())));

        int rowCount = 4;
        Set<String> devises = new HashSet<>();
        devises.addAll(totalParDeviseJMoinsUn.keySet());


        Row rowDeviseLibelle = sheet.createRow(rowCount);
        createCell(rowDeviseLibelle, 0, " ", totalStyle);
        int deviseCount = 1;
        for (String devise : devises) {
            createCell(rowDeviseLibelle, deviseCount, devise, totalStyle);
            deviseCount++;
        }

        rowCount++;


        Row rowDeviseAmountJ1 = sheet.createRow(rowCount);
        createCell(rowDeviseAmountJ1, 0, "TOTAL PAR DEVISE", totalStyle);
        int deviseAmountJ1 = 1;
        for (String devise : devises) {
            createCell(rowDeviseAmountJ1, deviseAmountJ1, totalParDeviseJMoinsUn.containsKey(devise) ? totalParDeviseJMoinsUn.get(devise) : 0L, dataLineStyle);
            deviseAmountJ1++;
        }

    }

    public void writeDataLinesForReportOne() {
        int rowCount = 9;
        Row enteteTableau = sheet.createRow(rowCount);


        createCell(enteteTableau, 0, "N°", libelleTableauStyle);
        createCell(enteteTableau, 1, "Code", libelleTableauStyle);
        createCell(enteteTableau, 2, "Libellé", libelleTableauStyle);
        createCell(enteteTableau, 3, "Devise", libelleTableauStyle);
        createCell(enteteTableau, 4, "J-2", libelleTableauStyle);
        createCell(enteteTableau, 5, "J-1", libelleTableauStyle);
        createCell(enteteTableau, 6, "Variation", libelleTableauStyle);
        rowCount++;

        Collections.sort(reportOneProjections);
        int index = 0;
        if (!reportOneProjections.isEmpty()) {
            for (ReportOneProjection reportOneProjection : reportOneProjections) {
                Row row = sheet.createRow(rowCount);


                createCell(row, 0, ++index, dataLineStyleCentered);
                createCell(row, 1, reportOneProjection.getCode(), dataLineStyle);
                createCell(row, 2, reportOneProjection.getLibelle(), dataLineStyle);
                createCell(row, 3, reportOneProjection.getCurrency(), dataLineStyle);
                createCell(row, 4, reportOneProjection.getMontantRecetteJMoinsDeux(), dataLineStyle);
                createCell(row, 5, reportOneProjection.getMontantRecetteJMoinsUn(), dataLineStyle);
                createCell(row, 6, reportOneProjection.getVariation(), dataLineStyle);
                rowCount++;
            }
        }

    }

    public void writeDataLinesForReportTwo() {
        int rowCount = 9;
        Row enteteTableau = sheet.createRow(rowCount);


        createCell(enteteTableau, 0, "N°", libelleTableauStyle);
        createCell(enteteTableau, 1, "Code", libelleTableauStyle);
        createCell(enteteTableau, 2, "Libellé", libelleTableauStyle);
        createCell(enteteTableau, 3, "Devise", libelleTableauStyle);
        createCell(enteteTableau, 4, "Montant", libelleTableauStyle);
        rowCount++;

        Collections.sort(reportOneProjections);
        int index = 0;
        if (!reportOneProjections.isEmpty()) {
            for (ReportOneProjection reportOneProjection : reportOneProjections) {
                Row row = sheet.createRow(rowCount);


                createCell(row, 0, ++index, dataLineStyleCentered);
                createCell(row, 1, reportOneProjection.getCode(), dataLineStyle);
                createCell(row, 2, reportOneProjection.getLibelle(), dataLineStyle);
                createCell(row, 3, reportOneProjection.getCurrency(), dataLineStyle);
                createCell(row, 4, reportOneProjection.getMontantRecetteCollecte(), dataLineStyle);
                rowCount++;
            }
        }

    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    public void exportReportOne(HttpServletResponse response, ReportOneForm reportOneForm) throws IOException {
        writeHeaderLine(reportOneForm);
        writeSummary();
        writeDataLinesForReportOne();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
//
        outputStream.close();

    }

    public void exportReportTwo(HttpServletResponse response, ReportTwoDTO reportTwoDTO) throws IOException {
        writeHeaderLineTwoReport(reportTwoDTO);
        writeSummaryForReportTwo();
        writeDataLinesForReportTwo();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
//
        outputStream.close();

    }

    public void setReportOneProjections(List<ReportOneProjection> reportOneProjections) {
        this.reportOneProjections = reportOneProjections;
    }

    private void setCellStyleAllBorders(CellStyle cellStyle) {
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
    }
}
