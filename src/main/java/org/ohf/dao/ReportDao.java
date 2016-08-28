package org.ohf.dao;

import org.ohf.bean.DTO.DisbursementDTO;

import java.util.List;

/**
 * Created by Laurie on 8/28/2016.
 */
public interface ReportDao {
    public List<DisbursementDTO> generateDisbursementReport();
}
