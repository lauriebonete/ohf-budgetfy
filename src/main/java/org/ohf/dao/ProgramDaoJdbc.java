package org.ohf.dao;

import org.ohf.bean.DTO.ProgramActivityDTO;
import org.ohf.bean.DTO.TotalProgramDTO;
import org.ohf.bean.Program;

import java.util.Date;
import java.util.List;

/**
 * Created by Laurie on 23 Oct 2016.
 */
public interface ProgramDaoJdbc {
    public List<ProgramActivityDTO> getProgramActivity(Date fromDate, Date toDate);
    public List<TotalProgramDTO> getTotalProgram(String year);
    public List<TotalProgramDTO> getTotalPerProgram(String year, Long programId);
    public List<Program> getActualBudgetPerProgram(String year);
}
