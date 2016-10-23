package org.ohf.service.impl;

import org.evey.service.impl.BaseCrudServiceImpl;
import org.ohf.bean.DTO.ProgramActivityDTO;
import org.ohf.bean.Program;
import org.ohf.dao.ProgramDaoJdbc;
import org.ohf.service.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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
}
