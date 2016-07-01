package org.ohf.service.impl;

import org.evey.service.impl.BaseCrudServiceImpl;
import org.ohf.bean.Program;
import org.ohf.service.ProgramService;
import org.springframework.stereotype.Service;

/**
 * Created by Laurie on 7/2/2016.
 */
@Service("programService")
public class ProgramServiceImpl extends BaseCrudServiceImpl<Program> implements ProgramService {
}
