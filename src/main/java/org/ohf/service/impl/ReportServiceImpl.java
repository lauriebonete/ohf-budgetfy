package org.ohf.service.impl;

import org.ohf.bean.DTO.DisbursementDTO;
import org.ohf.dao.ReportDao;
import org.ohf.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Laurie on 8/28/2016.
 */
@Service("reportService")
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportDao reportDao;

    @Override
    public List<DisbursementDTO> generateDisbursementReport() {
        return reportDao.generateDisbursementReport();
    }
}
