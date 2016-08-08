package org.ohf.service.impl;

import org.evey.service.impl.BaseCrudServiceImpl;
import org.ohf.bean.Expense;
import org.ohf.bean.Program;
import org.ohf.service.ExpenseService;
import org.ohf.service.ProgramService;
import org.springframework.stereotype.Service;

/**
 * Created by Laurie on 7/2/2016.
 */
@Service("expenseService")
public class ExpenseImpl extends BaseCrudServiceImpl<Expense> implements ExpenseService {
}
