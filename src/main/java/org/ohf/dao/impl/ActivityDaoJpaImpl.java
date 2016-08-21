package org.ohf.dao.impl;

import org.evey.dao.impl.BaseEntityDaoJpaImpl;
import org.ohf.bean.Activity;
import org.ohf.bean.DTO.ActivityExpenseDTO;
import org.ohf.dao.ActivityDao;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Laurie on 7/2/2016.
 */
@Repository("activityDao")
public class ActivityDaoJpaImpl extends BaseEntityDaoJpaImpl<Activity,Long> implements ActivityDao {

    @Override
    public List<ActivityExpenseDTO> findActivityExpense(Long programId, String queryName) {

        String queryString = getNamedQuery(queryName);
        Query query = getEntityManager().createQuery(queryString);
        query.setParameter("programId",programId);
        List<ActivityExpenseDTO> results = query.getResultList();
        return results;
    }

    @Override
    public Long countActivityExpense(Long activityId) {

        String queryString = "SELECT count(obj.id) FROM Particular obj WHERE obj.activity.id = :activityId";
        Query query = getEntityManager().createQuery(queryString);
        query.setParameter("activityId",activityId);

        Object count = query.getSingleResult();
        if (count != null){
            return (Long) count;
        }

        return 0L;
    }
}
