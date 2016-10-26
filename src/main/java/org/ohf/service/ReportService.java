package org.ohf.service;

import org.ohf.bean.DTO.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by Laurie on 8/28/2016.
 */
public interface ReportService {
    public Map<String, List<PeriodHelper>> prepareHelpers(List<DisbursementDTO> disbursementDTOs) throws Exception;
    public List<ProgramHelper> prepareProgramHelper(List<ProgramActivityDTO> programActivityDTOs) throws Exception;
    public void createDisbursementByDateRange(HttpServletResponse response, Map<String, List<PeriodHelper>> periodHelperMap, List<ProgramHelper> programHelperList) throws Exception;
}
