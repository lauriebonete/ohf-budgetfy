package org.ohf.service.impl;

import org.evey.service.impl.BaseCrudServiceImpl;
import org.ohf.bean.UserAccess;
import org.ohf.service.UserAccessService;
import org.springframework.stereotype.Service;

/**
 * Created by Laurie on 7/2/2016.
 */
@Service("userAccessService")
public class UserAccessServiceImpl extends BaseCrudServiceImpl<UserAccess> implements UserAccessService {
}
