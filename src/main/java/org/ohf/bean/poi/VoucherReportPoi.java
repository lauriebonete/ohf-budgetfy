package org.ohf.bean.poi;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.evey.bean.ReferenceLookUp;
import org.evey.bean.report.Report;
import org.ohf.bean.DTO.ParticularDTO;
import org.ohf.bean.Voucher;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Laurie on 10 Oct 2016.
 */
public class VoucherReportPoi extends Report {

    private List<ParticularDTO> particularDTOList;
    private Voucher voucher;
    private ReferenceLookUp status;

    public VoucherReportPoi(List<ParticularDTO> particularDTOList, Voucher voucher, ReferenceLookUp referenceLookUp) {
        this.particularDTOList = particularDTOList;
        this.voucher = voucher;
        this.status = referenceLookUp;
    }

    @Override
    protected void publishHeader() {
        XSSFCellStyle alignStyle = workbook.createCellStyle();
        XSSFFont boldFont = workbook.createFont();
        boldFont.setBold(true);
        alignStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        alignStyle.setFont(boldFont);

        XSSFCellStyle thousandSeparator = workbook.createCellStyle();
        thousandSeparator.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("#,##0.00"));

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

        sheet = workbook.createSheet("Particulars");
        XSSFCell cell = null;


        XSSFRow row = sheet.createRow(0);
        cell = row.createCell(1);
        cell.setCellValue("Status:");
        cell.setCellStyle(alignStyle);
        cell = row.createCell(2);
        cell.setCellValue(status.getValue());

        row = sheet.createRow(1);
        cell = row.createCell(1);
        cell.setCellValue("Payee:");
        cell.setCellStyle(alignStyle);
        cell = row.createCell(2);
        cell.setCellValue(voucher.getPayee());
        cell = row.createCell(3);
        cell.setCellValue("Check/Ref#:");
        cell.setCellStyle(alignStyle);
        cell = row.createCell(4);
        cell.setCellValue(voucher.getReference());

        row = sheet.createRow(2);
        cell = row.createCell(1);
        cell.setCellValue("Date:");
        cell.setCellStyle(alignStyle);
        cell = row.createCell(2);
        cell.setCellValue(dateFormat.format(voucher.getDate()));
        cell = row.createCell(3);
        cell.setCellValue("VC#:");
        cell.setCellStyle(alignStyle);
        cell = row.createCell(4);
        cell.setCellValue(voucher.getVcNumber());

        row = sheet.createRow(3);
        cell = row.createCell(1);
        cell.setCellValue("Total Amount:");
        cell.setCellStyle(alignStyle);
        cell = row.createCell(2);
        cell.setCellValue(voucher.getTotalAmount().doubleValue());
        cell.setCellStyle(thousandSeparator);
        cell = row.createCell(3);
        cell.setCellValue("Variance:");
        cell.setCellStyle(alignStyle);
        cell = row.createCell(4);
        cell.setCellValue(voucher.getTotalAmount().doubleValue() - voucher.getTotalExpense().doubleValue());
        cell.setCellStyle(thousandSeparator);


        row = sheet.createRow(5);
        cell = row.createCell(0);
        cell.setCellValue("#");
        cell.setCellStyle(alignStyle);

        cell = row.createCell(1);
        cell.setCellValue("Program");
        cell.setCellStyle(alignStyle);

        cell = row.createCell(2);
        cell.setCellValue("Activity");
        cell.setCellStyle(alignStyle);

        cell = row.createCell(3);
        cell.setCellValue("Particular Name");
        cell.setCellStyle(alignStyle);

        cell = row.createCell(4);
        cell.setCellValue("Expense");
        cell.setCellStyle(alignStyle);
    }

    @Override
    protected void publishData() {

        XSSFCellStyle thousandSeparator = workbook.createCellStyle();
        thousandSeparator.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("#,##0.00"));

        int indexRow = 6;
        int initial = indexRow;
        for(int i=0; i<particularDTOList.size();i++){
            ParticularDTO particularDTO = particularDTOList.get(i);
            XSSFRow row = sheet.createRow(indexRow);
            XSSFCell cell = null;

            cell = row.createCell(0);
            cell.setCellValue(i+1);

            cell = row.createCell(1);
            cell.setCellValue(particularDTO.getProgramName());

            cell = row.createCell(2);
            cell.setCellValue(particularDTO.getActivityName());

            cell = row.createCell(3);
            cell.setCellValue(particularDTO.getParticularName());

            cell = row.createCell(4);
            cell.setCellValue(particularDTO.getExpense().doubleValue());
            cell.setCellStyle(thousandSeparator);

            indexRow++;
        }

        XSSFCellStyle alignStyle = workbook.createCellStyle();
        XSSFFont boldFont = workbook.createFont();
        boldFont.setBold(true);
        alignStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        alignStyle.setFont(boldFont);

        XSSFRow row = sheet.createRow(indexRow+2);
        XSSFCell cell = null;

        cell = row.createCell(3);
        cell.setCellValue("TOTAL");
        cell.setCellStyle(alignStyle);

        StringBuilder formulaBuilder = new StringBuilder();
        formulaBuilder.append("SUM(E"+(initial+1))
                .append(":E")
                .append(indexRow + ")");

        cell = row.createCell(4);
        cell.setCellType(XSSFCell.CELL_TYPE_FORMULA);
        cell.setCellFormula(formulaBuilder.toString());
        cell.setCellStyle(thousandSeparator);

        sheet.setColumnWidth(0, 1500);
        sheet.setColumnWidth(1, 5500);
        sheet.setColumnWidth(2, 5500);
        sheet.setColumnWidth(3, 5500);
        sheet.setColumnWidth(4, 4500);


    }
}
