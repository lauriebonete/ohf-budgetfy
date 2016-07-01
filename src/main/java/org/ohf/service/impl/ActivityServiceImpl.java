package org.ohf.service.impl;

import org.evey.service.impl.BaseCrudServiceImpl;
import org.ohf.bean.Activity;
import org.ohf.service.ActivityService;
import org.springframework.stereotype.Service;

/**
 * Created by Laurie on 7/2/2016.
 */
@Service("activityService")
public class ActivityServiceImpl extends BaseCrudServiceImpl<Activity> implements ActivityService {
}
