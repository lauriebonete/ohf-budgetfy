package org.ohf.bean;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.evey.bean.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Kenneth on 7/2/2016.
 */
@Entity
@Table(name = "EXPENSE")
public class Expense extends BaseEntity {

}
