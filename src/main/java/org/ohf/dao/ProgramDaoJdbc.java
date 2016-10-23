package org.ohf.dao;

import org.ohf.bean.DTO.ProgramActivityDTO;

import java.util.Date;
import java.util.List;

/**
 * Created by Laurie on 23 Oct 2016.
 */
public interface ProgramDaoJdbc {
    public List<ProgramActivityDTO> getProgramActivity(Date fromDate, Date toDate);

}
