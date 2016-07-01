package org.ohf.dao.impl;

import org.evey.dao.impl.BaseEntityDaoJpaImpl;
import org.ohf.bean.Particular;
import org.ohf.dao.ParticularDao;
import org.springframework.stereotype.Repository;

/**
 * Created by Laurie on 7/2/2016.
 */
@Repository("particularDao")
public class ParticularDaoJpaImpl extends BaseEntityDaoJpaImpl<Particular,Long> implements ParticularDao {
}
