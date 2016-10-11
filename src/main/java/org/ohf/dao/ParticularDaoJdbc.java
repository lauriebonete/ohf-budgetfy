package org.ohf.dao;

import org.ohf.bean.DTO.ParticularDTO;

import java.util.List;

/**
 * Created by Laurie on 12 Oct 2016.
 */
public interface ParticularDaoJdbc {

    public List<ParticularDTO> getParticularsByVoucher(Long voucherId);
}
