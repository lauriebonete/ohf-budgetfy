package org.ohf.service;

import org.evey.service.BaseCrudService;
import org.ohf.bean.Activity;
import org.ohf.bean.DTO.ActivityExpenseDTO;

import java.util.List;

/**
 * Created by Laurie on 7/2/2016.
 */
public interface ActivityService extends BaseCrudService<Activity> {

    public List<ActivityExpenseDTO> findActivityExpense(Long programId);
}
