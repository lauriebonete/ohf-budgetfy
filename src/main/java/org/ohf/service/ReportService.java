package org.ohf.service;

import org.ohf.bean.DTO.DisbursementDTO;
import org.ohf.bean.DTO.PeriodHelper;
import org.ohf.bean.DTO.SheetHelper;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by Laurie on 8/28/2016.
 */
public interface ReportService {
    public Map<String, List<PeriodHelper>> prepareHelpers(List<DisbursementDTO> disbursementDTOs) throws Exception;
    public void createDisbursementByDateRange(HttpServletResponse response, Map<String, List<PeriodHelper>> periodHelperMap) throws Exception;
}
