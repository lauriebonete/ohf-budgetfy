package org.ohf.service;

import org.evey.service.BaseCrudService;
import org.ohf.bean.DTO.ParticularDTO;
import org.ohf.bean.Particular;

import java.util.List;

/**
 * Created by Laurie on 7/2/2016.
 */
public interface ParticularService extends BaseCrudService<Particular> {

    public List<ParticularDTO> getParticularByVoucherId(Long voucherId);
}
