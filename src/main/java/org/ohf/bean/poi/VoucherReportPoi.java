package org.ohf.bean.poi;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.evey.bean.report.Report;
import org.ohf.bean.DTO.ParticularDTO;

import java.util.List;

/**
 * Created by Laurie on 10 Oct 2016.
 */
public class VoucherReportPoi extends Report {

    private List<ParticularDTO> particularDTOList;

    public VoucherReportPoi(List<ParticularDTO> particularDTOList) {
        this.particularDTOList = particularDTOList;
    }

    @Override
    protected void publishHeader() {
        XSSFCellStyle alignStyle = workbook.createCellStyle();
        XSSFFont boldFont = workbook.createFont();
        boldFont.setBold(true);
        alignStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        alignStyle.setFont(boldFont);

        sheet = workbook.createSheet("Particulars");
        XSSFCell cell = null;

        XSSFRow row = sheet.createRow(0);
        cell = row.createCell(0);
        cell.setCellValue("#");
        cell.setCellStyle(alignStyle);

        cell = row.createCell(1);
        cell.setCellValue("Particular Name");
        cell.setCellStyle(alignStyle);

        cell = row.createCell(2);
        cell.setCellValue("Program");
        cell.setCellStyle(alignStyle);

        cell = row.createCell(3);
        cell.setCellValue("Activity");
        cell.setCellStyle(alignStyle);

        cell = row.createCell(4);
        cell.setCellValue("Expense");
        cell.setCellStyle(alignStyle);
    }

    @Override
    protected void publishData() {

        XSSFCellStyle thousandSeparator = workbook.createCellStyle();
        thousandSeparator.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("#,##0.00"));

        int indexRow = 1;
        for(int i=0; i<particularDTOList.size();i++){
            ParticularDTO particularDTO = particularDTOList.get(i);
            XSSFRow row = sheet.createRow(i+1);
            XSSFCell cell = null;

            cell = row.createCell(0);
            cell.setCellValue(i+1);

            cell = row.createCell(1);
            cell.setCellValue(particularDTO.getParticularName());

            cell = row.createCell(2);
            cell.setCellValue(particularDTO.getProgramName());

            cell = row.createCell(3);
            cell.setCellValue(particularDTO.getActivityName());

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
        formulaBuilder.append("SUM(E")
                .append("2:E")
                .append(indexRow+")");

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
