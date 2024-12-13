package com.sml.smartledger.Services.implementetion.exportServices;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.sml.smartledger.Model.party.PartyTransaction;
import com.sml.smartledger.Model.party.TransactionType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Service;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;



@Service
public class ReportGenerationService {

    public ByteArrayInputStream generateExcelReport(List<PartyTransaction> transactions,String businessName) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Transactions");

            // Add business name
            Row businessNameRow = sheet.createRow(0);
            org.apache.poi.ss.usermodel.Cell businessNameCell = businessNameRow.createCell(0);
            businessNameCell.setCellValue(businessName);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));

            Row headerRow = sheet.createRow(1);
            String[] headers = {"Date", "Customer Name", "Details", "You Gave", "You Got"};
            for (int i = 0; i < headers.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            int rowNum = 1;
            for (PartyTransaction transaction : transactions) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(transaction.getTransactionDate().toString());
                row.createCell(1).setCellValue(transaction.getParty().getName());
                row.createCell(2).setCellValue(transaction.getDescription());
                row.createCell(3).setCellValue(transaction.getTransactionType() == TransactionType.DEBIT ? transaction.getAmount() : 0);
                row.createCell(4).setCellValue(transaction.getTransactionType() == TransactionType.CREDIT ? transaction.getAmount() : 0);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }


    public ByteArrayInputStream generatePdfReport(List<PartyTransaction> transactions,String businessName) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // Set document margins
        document.setMargins(20, 20, 40, 20);
        // Add business name
        Paragraph businessNameParagraph = new Paragraph(businessName)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(18)
                .setBold()
                .setFontColor(ColorConstants.BLACK);
        document.add(businessNameParagraph);
        // Add title
        Paragraph title = new Paragraph("Transaction Report")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(24)
                .setBold()
                .setFontColor(ColorConstants.BLUE);
        document.add(title);

        // Add subtitle
        Paragraph subtitle = new Paragraph("Summary of all transactions")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(14)
                .setFontColor(ColorConstants.GRAY)
                .setMarginBottom(20);
        document.add(subtitle);

        // Create table with column widths
        float[] columnWidths = {2, 4, 4, 2, 2};
        Table table = new Table(UnitValue.createPercentArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100));

        // Add table headers
        String[] headers = {"Date", "Customer Name", "Details", "You Gave", "You Got"};
        for (String header : headers) {
            table.addHeaderCell(new Cell()
                    .add(new Paragraph(header).setBold().setFontColor(ColorConstants.WHITE))
                    .setBackgroundColor(ColorConstants.DARK_GRAY)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setPadding(5));
        }

        // Date formatter
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Add table rows with alternate row colors
        boolean isAlternate = false;
        Color alternateRowColor = ColorConstants.LIGHT_GRAY;
        for (PartyTransaction transaction : transactions) {
            Color rowColor = isAlternate ? alternateRowColor : ColorConstants.WHITE;

            table.addCell(new Cell().add(new Paragraph(transaction.getTransactionDate().toInstant()
                            .atZone(java.time.ZoneId.systemDefault())
                            .toLocalDate()
                            .format(dateFormatter)))
                    .setBackgroundColor(rowColor).setTextAlignment(TextAlignment.CENTER));
            table.addCell(new Cell().add(new Paragraph(transaction.getParty().getName()))
                    .setBackgroundColor(rowColor).setTextAlignment(TextAlignment.CENTER));
            table.addCell(new Cell().add(new Paragraph(transaction.getDescription()))
                    .setBackgroundColor(rowColor).setTextAlignment(TextAlignment.LEFT));
            table.addCell(new Cell().add(new Paragraph(transaction.getTransactionType() == TransactionType.DEBIT ?
                            String.valueOf(transaction.getAmount()) : "0"))
                    .setBackgroundColor(rowColor).setTextAlignment(TextAlignment.RIGHT));
            table.addCell(new Cell().add(new Paragraph(transaction.getTransactionType() == TransactionType.CREDIT ?
                            String.valueOf(transaction.getAmount()) : "0"))
                    .setBackgroundColor(rowColor).setTextAlignment(TextAlignment.RIGHT));

            isAlternate = !isAlternate;
        }

        document.add(table);

        // Add footer
        Paragraph footer = new Paragraph("Report generated on: " + java.time.LocalDate.now().format(dateFormatter))
                .setFontSize(10)
                .setFontColor(ColorConstants.GRAY)
                .setTextAlignment(TextAlignment.RIGHT)
                .setMarginTop(20);
        document.add(footer);

        document.close();
        return new ByteArrayInputStream(out.toByteArray());
    }
}