package org.ohf.dao.impl;

import org.evey.dao.impl.BaseEntityDaoJpaImpl;
import org.ohf.bean.Activity;
import org.ohf.dao.ActivityDao;
import org.springframework.stereotype.Repository;

/**
 * Created by Laurie on 7/2/2016.
 */
@Repository("activityDao")
public class ActivityDaoJpaImpl extends BaseEntityDaoJpaImpl<Activity,Long> implements ActivityDao {
}
