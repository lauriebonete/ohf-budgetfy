package org.ohf.controller;

import org.evey.bean.ReferenceLookUp;
import org.evey.service.ReferenceLookUpService;
import org.evey.utility.DateUtil;
import org.ohf.bean.DTO.*;
import org.ohf.bean.Program;
import org.ohf.bean.Voucher;
import org.ohf.bean.poi.DisbursementReportPoi;
import org.ohf.bean.poi.VoucherReportPoi;
import org.ohf.service.ParticularService;
import org.ohf.service.ProgramService;
import org.ohf.service.ReportService;
import org.ohf.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by Laurie on 8/28/2016.
 */
@Controller
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private ParticularService particularService;

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private ReferenceLookUpService referenceLookUpService;

    @Autowired
    private ProgramService programService;

    @RequestMapping
    public ModelAndView loadHtml() {
        return new ModelAndView("html/report.html");
    }

    @RequestMapping(value = "/create-disbursement", method = RequestMethod.GET)
    public void createDisbursementReport(HttpServletRequest request, HttpServletResponse response/*,
                                         @PathVariable("fromDate") Date fromDate,
                                         @PathVariable("toDate") Date toDate*/) throws Exception{
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDate = dateFormat.parse(request.getParameter("fromDate"));
        Date toDate = dateFormat.parse(request.getParameter("toDate"));
        String fileName = "Cash_Disbursement";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename="+fileName+".xlsx");

        List<DisbursementDTO> disbursementDTOList = voucherService.getDisbursementReportDetails(fromDate, toDate);
        List<ProgramActivityDTO> programActivityDTOList = programService.getProgramActivityByRange(fromDate, toDate);
        Map<String, List<PeriodHelper>> periodHelperMap =reportService.prepareHelpers(disbursementDTOList);
        List<ProgramHelper> programHelperList = reportService.prepareProgramHelper(programActivityDTOList);

        try {
            reportService.createDisbursementByDateRange(response, periodHelperMap, programHelperList);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/create-voucher/{id}", method = RequestMethod.GET)
    public void createVoucherReport(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") Long voucherId) throws Exception{
        String fileName = "Voucher_Report";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename="+fileName+".xlsx");

        List<ParticularDTO> results = particularService.getParticularByVoucherId(voucherId);
        Voucher voucher = voucherService.load(voucherId);
        ReferenceLookUp status = referenceLookUpService.load(voucher.getStatusId());
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            new VoucherReportPoi(results, voucher, status).publishReport(out);
            out.flush();
        } finally {
            if(out != null)
                out.close();
        }
    }

    @RequestMapping(value = "/program-total", method = RequestMethod.GET)
    public void createTotalPerProgramReport(HttpServletRequest request, HttpServletResponse response) throws Exception{


        String year = request.getParameter("year");
        Long programId = !request.getParameter("programId").equals(0)  ? Long.parseLong(request.getParameter("programId")) : null;
        String programName = request.getParameter("programName");


        StringBuilder fileName = new StringBuilder();
        if(programId!=null){
            fileName.append("TOTAL_FOR_")
                    .append(year)
                    .append("_")
                    .append(programName)
                    .append(".xlsx");
        }
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename="+fileName.toString());

        Program lookFor = new Program();
        lookFor.setYear(year);

        List<Program> programList = programService.findEntity(lookFor);
        List<TotalProgramDTO> totalProgramDTOList = programService.getTotalProgram(year);

        try {
           reportService.createTotalAllProgram(response, programList, totalProgramDTOList, year);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
