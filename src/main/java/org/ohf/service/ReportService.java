package org.ohf.service;

import org.ohf.bean.DTO.DisbursementDTO;

import java.util.List;

/**
 * Created by Laurie on 8/28/2016.
 */
public interface ReportService {
    public List<DisbursementDTO> generateDisbursementReport();
}
