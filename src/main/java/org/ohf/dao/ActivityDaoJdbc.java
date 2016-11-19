package org.ohf.dao;

import org.ohf.bean.Activity;

import java.util.List;

/**
 * Created by Laurie on 20 Nov 2016.
 */
public interface ActivityDaoJdbc {
    public List<Activity> getActualExpensePerActivity(Long programId);
}
