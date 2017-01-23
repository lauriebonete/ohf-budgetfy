package org.ohf.service.impl;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellAlignment;
import org.ohf.bean.Activity;
import org.ohf.bean.DTO.*;
import org.ohf.bean.Program;
import org.ohf.dao.ReportDao;
import org.ohf.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

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
        Map<String, List<PeriodHelper>> periodHelperMap = new LinkedHashMap<>();
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
                } else {
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
                    periodHelpers.add(periodHelper);
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
    public List<ProgramHelper> prepareProgramHelper(List<ProgramActivityDTO> programActivityDTOs) throws Exception {
        List<ProgramHelper> programHelpers = new ArrayList<>();

        for(ProgramActivityDTO programActivityDTO: programActivityDTOs){
            ProgramHelper test = new ProgramHelper();
            test.setProgramId(programActivityDTO.getProgramId());

            if(programHelpers.contains(test)){
                ProgramHelper programHelper = programHelpers.get(programHelpers.indexOf(test));

                Activity activity = new Activity();
                activity.setId(programActivityDTO.getActivityId());
                activity.setActivityName(programActivityDTO.getActivityName());

                programHelper.getActivityList().add(activity);
            } else {
                ProgramHelper programHelper = new ProgramHelper();
                programHelper.setProgramId(programActivityDTO.getProgramId());
                programHelper.setProgramName(programActivityDTO.getProgramName());
                programHelper.setActivityList(new ArrayList<Activity>());
                programHelper.setHexColor(programActivityDTO.getHexColor());

                Activity activity = new Activity();
                activity.setId(programActivityDTO.getActivityId());
                activity.setActivityName(programActivityDTO.getActivityName());

                programHelper.getActivityList().add(activity);
                programHelpers.add(programHelper);
            }
        }

        return programHelpers;
    }

    @Override
    public void createDisbursementByDateRange(HttpServletResponse response, Map<String, List<PeriodHelper>> periodHelperMap, List<ProgramHelper> programHelperList) throws Exception{
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFCellStyle thousandSeparator = workbook.createCellStyle();
        thousandSeparator.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("#,##0.00"));
        for (Map.Entry<String, List<PeriodHelper>> map : periodHelperMap.entrySet()) {
            XSSFSheet sheet = workbook.createSheet(map.getKey());
            createSheetHeader(sheet, map.getKey());
            List<DisbursementHeaderHelper> disbursementHeaderHelperList = createColumnHeader(sheet, workbook, programHelperList);

            int rowIndex = 5;
            int sequence = 1;
            for(PeriodHelper periodHelper:map.getValue()){
                createDataRows(sheet, periodHelper, rowIndex, sequence, thousandSeparator);
                createDataRowsActivity(sheet, periodHelper, disbursementHeaderHelperList, rowIndex, thousandSeparator);
                rowIndex++;
                sequence++;
            }

            StringBuilder formulaBuilder = new StringBuilder();
            formulaBuilder.append("SUM(G"+6)
                    .append(":G")
                    .append(rowIndex + ")");

            XSSFRow row = sheet.createRow(rowIndex);
            XSSFCell cell = row.createCell(6);
            cell.setCellType(XSSFCell.CELL_TYPE_FORMULA);
            cell.setCellFormula(formulaBuilder.toString());
            cell.setCellStyle(thousandSeparator);

            sheet.createFreezePane(7,0);

            int colSize = 0;
            for (ProgramHelper programHelper: programHelperList){
                colSize += programHelper.getActivityList().size();
            }
            colSize += programHelperList.size();

            int colIndex = 7;
            for(int i = 0; i<colSize;i++){
                StringBuilder buldier = new StringBuilder();
                buldier.append("SUM(" + CellReference.convertNumToColString(colIndex) + 6)
                        .append(":"+CellReference.convertNumToColString(colIndex))
                        .append(rowIndex + ")");

                row = sheet.getRow(rowIndex);
                cell = row.createCell(colIndex);
                cell.setCellType(XSSFCell.CELL_TYPE_FORMULA);
                cell.setCellFormula(buldier.toString());
                cell.setCellStyle(thousandSeparator);
                colIndex++;
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

    @Override
    public void createTotalAllProgram(HttpServletResponse response, List<Program> programList, List<TotalProgramDTO> totalProgramDTOList, String year) throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Total");

        XSSFCellStyle thousandSeparator = workbook.createCellStyle();
        thousandSeparator.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("#,##0.00"));

        XSSFCellStyle headerStyle = sheet.getWorkbook().createCellStyle();
        XSSFFont boldFont = sheet.getWorkbook().createFont();
        boldFont.setBold(true);

        headerStyle.setFont(boldFont);
        headerStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setWrapText(true);

        XSSFRow row = sheet.createRow(0);
        XSSFCell cell = row.createCell(0);
        cell.setCellValue("CASH DISBURSEMENT YEAR "+ year);

        row = sheet.createRow(2);
        cell = row.createCell(0);
        cell.setCellValue("MONTH");
        cell.setCellStyle(headerStyle);

        sheet.setColumnWidth(0, 4500);

        Map<Long, Integer> programIndex = new HashMap<>();
        int columnIndex = 1;
        for(Program program: programList){

            programIndex.put(program.getId(), columnIndex);

            Random r = new Random();
            XSSFCellStyle style = workbook.createCellStyle();
            Color color = Color.decode(program.getHexColor());
            XSSFColor myColor = new XSSFColor(color);
            style.setFillForegroundColor(myColor);
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            style.setFont(boldFont);
            style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setWrapText(true);

            cell = row.createCell(columnIndex);
            cell.setCellValue(program.getProgramName());
            cell.setCellStyle(style);
            sheet.setColumnWidth(columnIndex, 4500);
            columnIndex++;
        }

        for(int i=0, j=3; i<12; i++, j++){
            String monthName = new DateFormatSymbols().getMonths()[i];
            row = sheet.createRow(j);
            cell = row.createCell(0);
            cell.setCellValue(monthName);
            for(TotalProgramDTO totalProgramDTO: totalProgramDTOList){
                if(totalProgramDTO.getMonth()==i+1){
                    int index = programIndex.get(totalProgramDTO.getProgramId());
                    cell = row.createCell(index);
                    cell.setCellValue(totalProgramDTO.getExpense().doubleValue());
                    cell.setCellStyle(thousandSeparator);
                }
            }
        }
        row = sheet.createRow(15);
        cell = row.createCell(0);
        cell.setCellValue("TOTAL");

        for(int i=0, j=1; i<programList.size();i++,j++){
            StringBuilder buldier = new StringBuilder();
            buldier.append("SUM(" + CellReference.convertNumToColString(j) + 4)
                    .append(":"+CellReference.convertNumToColString(j))
                    .append(15 + ")");

            row = sheet.getRow(15);
            cell = row.createCell(j);
            cell.setCellType(XSSFCell.CELL_TYPE_FORMULA);
            cell.setCellFormula(buldier.toString());
            cell.setCellStyle(thousandSeparator);
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

    @Override
    public void createTotalPerProgram(HttpServletResponse response, List<Activity> activityList, List<TotalProgramDTO> totalProgramDTOList, String year, String programName) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Program");

        XSSFCellStyle thousandSeparator = workbook.createCellStyle();
        thousandSeparator.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("#,##0.00"));

        XSSFCellStyle headerStyle = sheet.getWorkbook().createCellStyle();
        XSSFFont boldFont = sheet.getWorkbook().createFont();
        boldFont.setBold(true);

        headerStyle.setFont(boldFont);
        headerStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setWrapText(true);

        XSSFRow row = sheet.createRow(0);
        XSSFCell cell = row.createCell(0);
        cell.setCellValue(programName);
        cell.setCellStyle(headerStyle);

        sheet.setColumnWidth(0, 4500);

        int columnIndex=1;
        Map<Long, Integer> activityIndex = new HashMap<>();
        for(Activity activity: activityList){
            cell = row.createCell(columnIndex);
            cell.setCellValue(activity.getActivityName());
            cell.setCellStyle(headerStyle);
            activityIndex.put(activity.getId(), columnIndex);
            sheet.setColumnWidth(columnIndex, 4500);
            columnIndex++;
        }
        cell = row.createCell(columnIndex);
        cell.setCellValue("TOTAL");
        cell.setCellStyle(headerStyle);
        sheet.setColumnWidth(columnIndex, 4500);

        for(int i=0, j=1; i<12; i++, j++){
            String monthName = new DateFormatSymbols().getMonths()[i];
            row = sheet.createRow(j);
            cell = row.createCell(0);
            cell.setCellValue(monthName);
            cell.setCellStyle(headerStyle);
            for(TotalProgramDTO totalProgramDTO: totalProgramDTOList){
                if(totalProgramDTO.getMonth()==i+1){
                    int index = activityIndex.get(totalProgramDTO.getActivityId());
                    cell = row.createCell(index);
                    cell.setCellValue(totalProgramDTO.getExpense().doubleValue());
                    cell.setCellStyle(thousandSeparator);
                }
            }

            StringBuilder buldier = new StringBuilder();
            buldier.append("SUM(B"+ (j+1) )
                    .append(":"+CellReference.convertNumToColString(columnIndex-1))
                    .append((j+1) + ")");

            cell = row.createCell(activityList.size()+1);
            cell.setCellType(XSSFCell.CELL_TYPE_FORMULA);
            cell.setCellFormula(buldier.toString());
            cell.setCellStyle(thousandSeparator);
        }

        row = sheet.createRow(13);
        for(int i=1; i<=activityList.size()+1;i++){
            StringBuilder buldier = new StringBuilder();
            buldier.append("SUM("+CellReference.convertNumToColString(i) + "2" )
                    .append(":" + CellReference.convertNumToColString(i))
                    .append("13)");

            cell = row.createCell(i);
            cell.setCellType(XSSFCell.CELL_TYPE_FORMULA);
            cell.setCellFormula(buldier.toString());
            cell.setCellStyle(thousandSeparator);
        }


        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            workbook.write(out);
            out.close();
        } catch (IOException exception){
            _log.error(exception.getMessage());
        } finally {
        }
    }

    @Override
    public void createExpectedVsActual(HttpServletResponse response, List<TotalProgramDTO> programDTOList, List<ProgramActivityDTO> programActivityDTOList, String programName) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(programName);

        XSSFCellStyle thousandSeparator = workbook.createCellStyle();
        thousandSeparator.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("#,##0.00"));

        XSSFCellStyle headerStyle = sheet.getWorkbook().createCellStyle();
        XSSFFont boldFont = sheet.getWorkbook().createFont();
        boldFont.setBold(true);

        headerStyle.setFont(boldFont);
        headerStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setWrapText(true);

        XSSFRow row = sheet.createRow(0);
        XSSFCell cell = row.createCell(0);
        cell.setCellValue("Program");
        cell.setCellStyle(headerStyle);

        cell = row.createCell(1);
        cell.setCellValue("Expected");
        cell.setCellStyle(headerStyle);

        cell = row.createCell(2);
        cell.setCellValue("Actual");
        cell.setCellStyle(headerStyle);

        cell = row.createCell(3);
        cell.setCellValue("Variance");
        cell.setCellStyle(headerStyle);

        int i = 1;
        for(TotalProgramDTO totalProgramDTO: programDTOList){

            BigDecimal totalBudget = totalProgramDTO.getTotalBudget() != null ? totalProgramDTO.getTotalBudget() : new BigDecimal(0);
            BigDecimal expense = totalProgramDTO.getExpense() != null ? totalProgramDTO.getExpense() : new BigDecimal(0);

            row = sheet.createRow(i);
            cell = row.createCell(0);
            cell.setCellValue(programName);

            cell = row.createCell(1);
            cell.setCellValue(totalBudget.doubleValue());
            cell.setCellStyle(thousandSeparator);

            cell = row.createCell(2);
            cell.setCellValue(expense.doubleValue());
            cell.setCellStyle(thousandSeparator);

            cell = row.createCell(3);
            cell.setCellValue(totalBudget.doubleValue() - expense.doubleValue());
            cell.setCellStyle(thousandSeparator);
            i++;
        }


        i++;

        row = sheet.createRow(i);
        cell = row.createCell(0);
        cell.setCellValue("Activity");
        cell.setCellStyle(headerStyle);

        cell = row.createCell(1);
        cell.setCellValue("Expected");
        cell.setCellStyle(headerStyle);

        cell = row.createCell(2);
        cell.setCellValue("Actual");
        cell.setCellStyle(headerStyle);

        cell = row.createCell(3);
        cell.setCellValue("Variance");
        cell.setCellStyle(headerStyle);


        for(ProgramActivityDTO programActivityDTO: programActivityDTOList){
            i++;
            row = sheet.createRow(i);
            cell = row.createCell(0);
            cell.setCellValue(programActivityDTO.getActivityName());

            cell = row.createCell(1);
            cell.setCellValue(programActivityDTO.getExpectedExpense().doubleValue());
            cell.setCellStyle(thousandSeparator);

            cell = row.createCell(2);
            cell.setCellValue(programActivityDTO.getActualExpense().doubleValue());
            cell.setCellStyle(thousandSeparator);

            cell = row.createCell(3);
            cell.setCellValue(programActivityDTO.getExpectedExpense().doubleValue() - programActivityDTO.getActualExpense().doubleValue());
            cell.setCellStyle(thousandSeparator);
        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);

        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            workbook.write(out);
            out.close();
        } catch (IOException exception){
            _log.error(exception.getMessage());
        } finally {
        }

    }

    private void createSheetHeader(XSSFSheet sheet, String key){
        XSSFCellStyle headerStyle = sheet.getWorkbook().createCellStyle();
        XSSFFont boldFont = sheet.getWorkbook().createFont();
        boldFont.setBold(true);

        headerStyle.setFont(boldFont);
        headerStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setWrapText(true);

        XSSFRow row = sheet.createRow(0);
        XSSFCell cell = null;
        cell = row.createCell(0);
        cell.setCellValue("Open Heart Foundation Worldwide, INC.");
        cell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,6));

        row = sheet.createRow(1);
        cell = row.createCell(0);
        cell.setCellValue("CASH DISBURSEMENT");
        cell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(1,1,0,6));

        row = sheet.createRow(2);
        cell = row.createCell(0);
        cell.setCellValue(key);
        cell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(2,2,0,6));
    }

    private List<DisbursementHeaderHelper> createColumnHeader(XSSFSheet sheet, XSSFWorkbook workbook, List<ProgramHelper> programHelperList){
        XSSFCellStyle headerStyle = workbook.createCellStyle();
        XSSFFont boldFont = workbook.createFont();
        boldFont.setBold(true);

        headerStyle.setFont(boldFont);
        headerStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setWrapText(true);

        XSSFRow row = sheet.createRow(4);
        XSSFCell cell = null;
        cell = row.createCell(0);
        cell.setCellValue("Seq");
        cell.setCellStyle(headerStyle);

        cell = row.createCell(1);
        cell.setCellValue("Date");
        cell.setCellStyle(headerStyle);

        cell = row.createCell(2);
        cell.setCellValue("Payee");
        cell.setCellStyle(headerStyle);

        cell = row.createCell(3);
        cell.setCellValue("Particulars");
        cell.setCellStyle(headerStyle);

        cell = row.createCell(4);
        cell.setCellValue("Reference #");
        cell.setCellStyle(headerStyle);

        cell = row.createCell(5);
        cell.setCellValue("Voucher #");
        cell.setCellStyle(headerStyle);

        cell = row.createCell(6);
        cell.setCellValue("Amount");
        cell.setCellStyle(headerStyle);

        sheet.setColumnWidth(0, 1500);
        sheet.setColumnWidth(1, 3000);
        sheet.setColumnWidth(2, 4500);
        sheet.setColumnWidth(3, 6500);
        sheet.setColumnWidth(4, 4500);
        sheet.setColumnWidth(5, 4500);
        sheet.setColumnWidth(6, 5000);

        int columnIndex = 7;
        List<DisbursementHeaderHelper> disbursementHeaderHelperList = new ArrayList<>();
        for(ProgramHelper programHelper: programHelperList){

            DisbursementHeaderHelper disbursementHeaderHelper = new DisbursementHeaderHelper();
            disbursementHeaderHelper.setProgramId(programHelper.getProgramId());
            disbursementHeaderHelper.setColumnIndex(columnIndex);
            disbursementHeaderHelper.setStartIndex(columnIndex + 1);
            disbursementHeaderHelper.setDisbursementActivityHelperList(new ArrayList<DisbursementActivityHelper>());

            Random r = new Random();
            XSSFCellStyle style = workbook.createCellStyle();
            Color color = Color.decode(programHelper.getHexColor());
            XSSFColor myColor = new XSSFColor(color);
            style.setFillForegroundColor(myColor);
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            style.setFont(boldFont);
            style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setWrapText(true);


            cell = row.createCell(columnIndex);
            cell.setCellValue(programHelper.getProgramName());
            cell.setCellStyle(style);
            sheet.setColumnWidth(columnIndex, 5000);
            columnIndex++;

            for(Activity activity:programHelper.getActivityList()){
                DisbursementActivityHelper disbursementActivityHelper = new DisbursementActivityHelper();
                disbursementActivityHelper.setActivityId(activity.getId());
                disbursementActivityHelper.setIndex(columnIndex);
                disbursementHeaderHelper.getDisbursementActivityHelperList().add(disbursementActivityHelper);

                cell = row.createCell(columnIndex);
                cell.setCellValue(activity.getActivityName());
                cell.setCellStyle(style);
                sheet.setColumnWidth(columnIndex, 5000);
                columnIndex++;
            }
            disbursementHeaderHelper.setEndIndex(columnIndex-1);
            disbursementHeaderHelperList.add(disbursementHeaderHelper);
        }
        return disbursementHeaderHelperList;
    }

    private void createDataRows(XSSFSheet sheet, PeriodHelper periodHelper, int rowNumber, int seq, XSSFCellStyle thousandSeparator){
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
        cell.setCellValue(periodHelper.getTotalExpense()!=null ? periodHelper.getTotalExpense().doubleValue():0);
        cell.setCellStyle(thousandSeparator);
    }

    private void createDataRowsActivity(XSSFSheet sheet, PeriodHelper periodHelper, List<DisbursementHeaderHelper> disbursementHeaderHelperList, int rowNumber, XSSFCellStyle thousandSeparator){
        XSSFRow row = sheet.getRow(rowNumber);
        XSSFCell cell = null;


        for(DisbursementDTO disbursementDTO: periodHelper.getDisbursementDTOList()){
            DisbursementHeaderHelper lookFor = new DisbursementHeaderHelper();
            lookFor.setProgramId(disbursementDTO.getProgramId());

            if(disbursementHeaderHelperList.indexOf(lookFor)>=0){
                DisbursementHeaderHelper disbursementHeaderHelper = disbursementHeaderHelperList.get(disbursementHeaderHelperList.indexOf(lookFor));

                cell = row.createCell(disbursementHeaderHelper.getColumnIndex());

                StringBuilder formulaBuilder = new StringBuilder();
                formulaBuilder.append("SUM("+ CellReference.convertNumToColString(disbursementHeaderHelper.getStartIndex())+(rowNumber+1))
                        .append(":"+ CellReference.convertNumToColString(disbursementHeaderHelper.getEndIndex()))
                        .append((rowNumber + 1) + ")");

                cell.setCellType(XSSFCell.CELL_TYPE_FORMULA);
                cell.setCellFormula(formulaBuilder.toString());
                cell.setCellStyle(thousandSeparator);

                DisbursementActivityHelper lookForActivity = new DisbursementActivityHelper();
                lookForActivity.setActivityId(disbursementDTO.getActivityId());

                if(disbursementHeaderHelper.getDisbursementActivityHelperList().indexOf(lookForActivity)>=0){
                    DisbursementActivityHelper disbursementActivityHelper = disbursementHeaderHelper.getDisbursementActivityHelperList().get(disbursementHeaderHelper.getDisbursementActivityHelperList().indexOf(lookForActivity));

                    cell = row.createCell(disbursementActivityHelper.getIndex());
                    cell.setCellValue(disbursementDTO.getExpense()!=null ? disbursementDTO.getExpense().doubleValue():0);
                    cell.setCellStyle(thousandSeparator);
                }
            }
        }
    }
}
