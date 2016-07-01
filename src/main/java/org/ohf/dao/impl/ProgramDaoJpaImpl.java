package org.ohf.dao.impl;

import org.evey.dao.impl.BaseEntityDaoJpaImpl;
import org.ohf.bean.Program;
import org.ohf.dao.ProgramDao;
import org.springframework.stereotype.Repository;

/**
 * Created by Laurie on 7/2/2016.
 */
@Repository("programDao")
public class ProgramDaoJpaImpl extends BaseEntityDaoJpaImpl<Program,Long> implements ProgramDao {
}
