package org.evey.dao.impl;

import org.evey.bean.FileDetail;
import org.evey.dao.FileDetailDao;
import org.springframework.stereotype.Repository;

/**
 * Created by Laurie on 11/24/2015.
 */
@Repository("fileDetailDao")
public class FileDetailDaoJpaImpl extends BaseEntityDaoJpaImpl<FileDetail,Long> implements FileDetailDao {
}
