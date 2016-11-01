package org.ohf.service;

import org.evey.service.BaseCrudService;
import org.ohf.bean.DTO.ProgramActivityDTO;
import org.ohf.bean.DTO.TotalProgramDTO;
import org.ohf.bean.Program;

import java.util.Date;
import java.util.List;

/**
 * Created by Laurie on 7/2/2016.
 */
public interface ProgramService extends BaseCrudService<Program> {
    public List<ProgramActivityDTO> getProgramActivityByRange(Date fromDate, Date toDate);
    public List<TotalProgramDTO> getTotalProgram(String year);
}
