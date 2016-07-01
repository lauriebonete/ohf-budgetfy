package org.ohf.dao.impl;

import org.evey.dao.impl.BaseEntityDaoJpaImpl;
import org.ohf.bean.UserAccess;
import org.ohf.dao.UserAccessDao;
import org.springframework.stereotype.Repository;

/**
 * Created by Laurie on 7/2/2016.
 */
@Repository("userAccessDao")
public class UserAccessDaoJpaImpl extends BaseEntityDaoJpaImpl<UserAccess,Long> implements UserAccessDao {
}
