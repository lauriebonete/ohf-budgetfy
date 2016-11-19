package org.ohf.service.impl;

import org.evey.service.impl.BaseCrudServiceImpl;
import org.ohf.bean.Activity;
import org.ohf.bean.DTO.ActivityExpenseDTO;
import org.ohf.dao.ActivityDao;
import org.ohf.dao.ActivityDaoJdbc;
import org.ohf.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Laurie on 7/2/2016.
 */
@Service("activityService")
public class ActivityServiceImpl extends BaseCrudServiceImpl<Activity> implements ActivityService {

    @Autowired
    private ActivityDao activityDao;

    @Autowired
    private ActivityDaoJdbc activityDaoJdbc;

    @Override
    public List<ActivityExpenseDTO> findActivityExpense(Long programId) {
        return activityDao.findActivityExpense(programId, "jpql.activity.find-program-activity-expense");
    }

    @Override
    public Long countActivityExpense(Long activityId) {
        return activityDao.countActivityExpense(activityId);
    }

    @Override
    public List<Activity> getActualExpensePerActivity(Long programId) {
        return activityDaoJdbc.getActualExpensePerActivity(programId);
    }
}
