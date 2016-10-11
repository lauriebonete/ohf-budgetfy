package org.ohf.controller;

import org.evey.utility.DateUtil;
import org.ohf.bean.DTO.DisbursementDTO;
import org.ohf.bean.DTO.ParticularDTO;
import org.ohf.bean.poi.DisbursementReportPoi;
import org.ohf.bean.poi.VoucherReportPoi;
import org.ohf.service.ParticularService;
import org.ohf.service.ReportService;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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

    @RequestMapping
    public ModelAndView loadHtml() {
        return new ModelAndView("html/report.html");
    }

    @RequestMapping(value = "/create-disbursement", method = RequestMethod.GET)
    public void createDisbursementReport(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String fileName = "Cash_Disbursement";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename="+fileName+".xlsx");

        List<DisbursementDTO> disbursementDTOList = reportService.generateDisbursementReport();
        disbursementDTOList.size();

        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            new DisbursementReportPoi(disbursementDTOList).publishReport(out);
            out.flush();
        } finally {
            if(out != null)
                out.close();
        }
    }

    @RequestMapping(value = "/create-voucher/{id}", method = RequestMethod.GET)
    public void createVoucherReport(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") Long voucherId) throws Exception{
        String fileName = "Voucher_Report";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename="+fileName+".xlsx");

        List<ParticularDTO> results = particularService.getParticularByVoucherId(voucherId);

        /*List<DisbursementDTO> disbursementDTOList = reportService.generateDisbursementReport();
        disbursementDTOList.size();*/

        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            new VoucherReportPoi(results).publishReport(out);
            out.flush();
        } finally {
            if(out != null)
                out.close();
        }
    }
}
