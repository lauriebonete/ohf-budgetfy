package org.ohf.service.impl;

import org.evey.service.impl.BaseCrudServiceImpl;
import org.ohf.bean.DTO.ParticularDTO;
import org.ohf.bean.Particular;
import org.ohf.dao.ParticularDaoJdbc;
import org.ohf.service.ParticularService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Laurie on 7/2/2016.
 */
@Service("particularService")
public class ParticularServiceImpl extends BaseCrudServiceImpl<Particular> implements ParticularService {

    @Autowired
    private ParticularDaoJdbc particularDaoJdbc;

    @Override
    public List<ParticularDTO> getParticularByVoucherId(Long voucherId) {
        return particularDaoJdbc.getParticularsByVoucher(voucherId);
    }
}
