package org.ohf.service.impl;

import org.apache.log4j.Logger;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.ohf.bean.DTO.DisbursementDTO;
import org.ohf.bean.DTO.PeriodHelper;
import org.ohf.bean.DTO.SheetHelper;
import org.ohf.dao.ReportDao;
import org.ohf.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Laurie on 8/28/2016.
 */
@Service("reportService")
public class ReportServiceImpl implements ReportService {

    private static Logger _log = Logger.getLogger(ReportServiceImpl.class);

    @Override
    public Map<String, List<PeriodHelper>> prepareHelpers(List<DisbursementDTO> disbursementDTOs) throws Exception{
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dateMonthFormat = new SimpleDateFormat("MMM-yyyy");
        Map<String, List<PeriodHelper>> periodHelperMap = new HashMap<>();
        for(DisbursementDTO disbursementDTO: disbursementDTOs){
            Date date = dateFormat.parse(disbursementDTO.getVoucherDate());
            String month = dateMonthFormat.format(date.getTime());
            if(periodHelperMap.get(month)!=null){
                List<PeriodHelper> periodHelpers = periodHelperMap.get(month);
                PeriodHelper test = new PeriodHelper();
                test.setVcId(disbursementDTO.getVoucherId());
                if (periodHelpers.contains(test)){
                    PeriodHelper periodHelper = periodHelpers.get(periodHelpers.indexOf(test));
                    periodHelper.setParticulars(periodHelper.getParticulars()+", "+disbursementDTO.getParticulars());
                    periodHelper.setTotalExpense(periodHelper.getTotalExpense().add(disbursementDTO.getExpense()));
                    periodHelper.getDisbursementDTOList().add(disbursementDTO);
                }
            } else {
                periodHelperMap.put(month, new ArrayList<PeriodHelper>());
                PeriodHelper periodHelper = new PeriodHelper();
                periodHelper.setVcId(disbursementDTO.getVoucherId());
                periodHelper.setVcDate(disbursementDTO.getVoucherDate());
                periodHelper.setPayee(disbursementDTO.getPayee());
                periodHelper.setParticulars(disbursementDTO.getParticulars());
                periodHelper.setReference(disbursementDTO.getReference());
                periodHelper.setVcNumber(disbursementDTO.getVcNumber());
                periodHelper.setTotalExpense(disbursementDTO.getExpense());
                periodHelper.setDisbursementDTOList(new ArrayList<DisbursementDTO>());
                periodHelper.getDisbursementDTOList().add(disbursementDTO);
                periodHelperMap.get(month).add(periodHelper);
            }

        }
        return periodHelperMap;
    }

    @Override
    public void createDisbursementByDateRange(HttpServletResponse response, Map<String, List<PeriodHelper>> periodHelperMap) throws Exception{
        XSSFWorkbook workbook = new XSSFWorkbook();
        for (Map.Entry<String, List<PeriodHelper>> map : periodHelperMap.entrySet()) {
            XSSFSheet sheet = workbook.createSheet(map.getKey());
            createSheetHeader(sheet, map.getKey());
            createColumnHeader(sheet);

            int row = 5;
            int sequence = 1;
            for(PeriodHelper periodHelper:map.getValue()){
                createDataRows(sheet, periodHelper, row, sequence);
                row++;
                sequence++;
            }
        }

        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            workbook.write(out);
        } catch (IOException exception){
            _log.error(exception.getMessage());
        } finally {
            out.close();
        }
    }

    private void createSheetHeader(XSSFSheet sheet, String key){
        XSSFRow row = sheet.createRow(0);
        XSSFCell cell = null;
        cell = row.createCell(0);
        cell.setCellValue("Open Heart Foundation Worldwide, INC.");
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,6));

        row = sheet.createRow(1);
        cell = row.createCell(0);
        cell.setCellValue("CASH DISBURSEMENT");
        sheet.addMergedRegion(new CellRangeAddress(1,1,0,6));

        row = sheet.createRow(2);
        cell = row.createCell(0);
        cell.setCellValue(key);
        sheet.addMergedRegion(new CellRangeAddress(2,2,0,6));
    }

    private void createColumnHeader(XSSFSheet sheet){
        XSSFRow row = sheet.createRow(4);
        XSSFCell cell = null;
        cell = row.createCell(0);
        cell.setCellValue("Seq");

        cell = row.createCell(1);
        cell.setCellValue("Date");

        cell = row.createCell(2);
        cell.setCellValue("Payee");

        cell = row.createCell(3);
        cell.setCellValue("Particulars");

        cell = row.createCell(4);
        cell.setCellValue("Reference #");

        cell = row.createCell(5);
        cell.setCellValue("Voucher #");

        cell = row.createCell(6);
        cell.setCellValue("Amount");
    }

    private void createDataRows(XSSFSheet sheet, PeriodHelper periodHelper, int rowNumber, int seq){
        XSSFRow row = sheet.createRow(rowNumber);
        XSSFCell cell = null;
        cell = row.createCell(0);
        cell.setCellValue(seq);

        cell = row.createCell(1);
        cell.setCellValue(periodHelper.getVcDate());

        cell = row.createCell(2);
        cell.setCellValue(periodHelper.getPayee());

        cell = row.createCell(3);
        cell.setCellValue(periodHelper.getParticulars());

        cell = row.createCell(4);
        cell.setCellValue(periodHelper.getReference());

        cell = row.createCell(5);
        cell.setCellValue(periodHelper.getVcNumber());

        cell = row.createCell(6);
        cell.setCellValue(periodHelper.getTotalExpense().doubleValue());
    }
}
