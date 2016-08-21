package org.ohf.dao;

import org.evey.dao.BaseEntityDao;
import org.ohf.bean.Activity;
import org.ohf.bean.DTO.ActivityExpenseDTO;

import java.util.List;

/**
 * Created by Laurie on 7/2/2016.
 */
public interface ActivityDao extends BaseEntityDao<Activity,Long> {
    public List<ActivityExpenseDTO> findActivityExpense(Long programId, String queryName);
    public Long countActivityExpense(Long activityId);
}
