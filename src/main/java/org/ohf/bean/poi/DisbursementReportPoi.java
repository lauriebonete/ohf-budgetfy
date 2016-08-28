package org.ohf.bean.poi;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.evey.bean.report.Report;
import org.ohf.bean.DTO.DisbursementDTO;

import java.util.List;

/**
 * Created by Laurie on 8/28/2016.
 */
public class DisbursementReportPoi extends Report {

    private List<DisbursementDTO> disbursementDTOList;

    public DisbursementReportPoi(List<DisbursementDTO> disbursementDTOList) {
        this.disbursementDTOList = disbursementDTOList;
    }

    @Override
    protected void publishHeader() {
        XSSFCellStyle alignStyle = workbook.createCellStyle();
        XSSFFont boldFont = workbook.createFont();
        boldFont.setBold(true);
        alignStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        alignStyle.setFont(boldFont);

        sheet = workbook.createSheet("Inventory");
        XSSFCell cell = null;

        XSSFRow row = sheet.createRow(0);
        cell = row.createCell(0);
        cell.setCellValue("Open Heart Foundation Worldwide, INC.");
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,6));

        row = sheet.createRow(1);
        cell = row.createCell(0);
        cell.setCellValue("CASH DISBURSEMENT");
        sheet.addMergedRegion(new CellRangeAddress(1,1,0,6));

        row = sheet.createRow(2);
        cell = row.createCell(0);
        cell.setCellValue("Date here");
        sheet.addMergedRegion(new CellRangeAddress(2,2,0,6));

        row = sheet.createRow(3);
        cell = row.createCell(0);
        cell.setCellValue("Seq");
        cell = row.createCell(1);
        cell.setCellValue("Voucher Date");
        cell = row.createCell(2);
        cell.setCellValue("Payee");
        cell = row.createCell(3);
        cell.setCellValue("Particulars");
        cell = row.createCell(4);
        cell.setCellValue("Reference");
        cell = row.createCell(5);
        cell.setCellValue("VC Number");
        cell = row.createCell(6);
        cell.setCellValue("Amount");

    }

    @Override
    protected void publishData() {
        int index = 4;
        for(DisbursementDTO disbursementDTO: disbursementDTOList){
            XSSFRow row = sheet.createRow(index);
            XSSFCell cell = null;
            cell = row.createCell(0);
            cell.setCellValue(disbursementDTO.getRow());
            cell = row.createCell(1);
            cell.setCellValue(disbursementDTO.getVoucherDate());
            cell = row.createCell(2);
            cell.setCellValue(disbursementDTO.getPayee());
            cell = row.createCell(3);
            cell.setCellValue(disbursementDTO.getParticulars());
            cell = row.createCell(4);
            cell.setCellValue(disbursementDTO.getReference());
            cell = row.createCell(5);
            cell.setCellValue(disbursementDTO.getVcNumber());
            cell = row.createCell(6);
            cell.setCellValue(disbursementDTO.getTotalAmount().doubleValue());

            index++;
        }
    }
}
