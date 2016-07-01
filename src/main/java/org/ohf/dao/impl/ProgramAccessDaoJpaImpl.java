package org.ohf.dao.impl;

import org.evey.dao.impl.BaseEntityDaoJpaImpl;
import org.ohf.bean.ProgramAccess;
import org.ohf.dao.ProgramAccessDao;
import org.springframework.stereotype.Repository;

/**
 * Created by Laurie on 7/2/2016.
 */
@Repository("programAccessDao")
public class ProgramAccessDaoJpaImpl extends BaseEntityDaoJpaImpl<ProgramAccess,Long> implements ProgramAccessDao {
}
