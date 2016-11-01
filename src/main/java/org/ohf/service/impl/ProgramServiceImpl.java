package org.ohf.service.impl;

import org.evey.service.impl.BaseCrudServiceImpl;
import org.ohf.bean.Activity;
import org.ohf.bean.DTO.ProgramActivityDTO;
import org.ohf.bean.DTO.TotalProgramDTO;
import org.ohf.bean.Program;
import org.ohf.dao.ProgramDaoJdbc;
import org.ohf.service.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Laurie on 7/2/2016.
 */
@Service("programService")
public class ProgramServiceImpl extends BaseCrudServiceImpl<Program> implements ProgramService {

    @Autowired
    private ProgramDaoJdbc programDaoJdbc;

    @Override
    public List<ProgramActivityDTO> getProgramActivityByRange(Date fromDate, Date toDate) {
        return programDaoJdbc.getProgramActivity(fromDate, toDate);
    }

    @Override
    public List<TotalProgramDTO> getTotalProgram(String year) {
        return programDaoJdbc.getTotalProgram(year);
    }

    @Override
    public List<TotalProgramDTO> getTotalPerProgram(String year, Long programId) {
        return programDaoJdbc.getTotalPerProgram(year, programId);
    }

    @Override
    public List<Activity> getAllActivityOfProgram(Long programId) {
        Map<String,Object> params = new HashMap<>();
        params.put("programId", programId);
        return (List<Activity>) getDao().findEntityByNamedQuery("jpql.program.get-activities", params, Activity.class);
    }
}
