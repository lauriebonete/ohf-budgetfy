package org.ohf.bean;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.evey.bean.BaseEntity;
import org.evey.bean.ReferenceLookUp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by Laurie on 7/2/2016.
 */
@Entity
@Table(name = "ACTIVITY")
public class Activity extends BaseEntity {

    @Column(name = "ACTIVITY_NAME")
    private String activityName;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "PROGRAM_ID", referencedColumnName = "ID")
    @JsonBackReference
    private Program program;

    @Column(name = "PROGRAM_ID", insertable = false, updatable = false)
    private Long programId;

    @OneToMany(mappedBy = "activity")
    @JsonManagedReference
    private List<Particular> particulars;

    @ManyToOne
    @JoinColumn(name = "ACTIVITY_TYPE_ID", referencedColumnName = "ID")
    private ReferenceLookUp activityType;

    @Column(name = "ACTIVITY_TYPE_ID", insertable = false, updatable = false)
    private Long activityTypeId;

    @ManyToOne
    @JoinColumn(name = "CODE_ID", referencedColumnName = "ID")
    private ReferenceLookUp activityCode;

    @Column(name = "CODE_ID", insertable = false, updatable = false)
    private Long activityCodeId;

    private transient String displayAmount;

    @Column(name = "ACTIVITY_CODE_NAME")
    private String activityCodeName;

    public String getActivityCodeName() {
        return activityCodeName;
    }

    public void setActivityCodeName(String activityCodeName) {
        this.activityCodeName = activityCodeName;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public List<Particular> getParticulars() {
        return particulars;
    }

    public void setParticulars(List<Particular> particulars) {
        this.particulars = particulars;
    }

    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    public ReferenceLookUp getActivityType() {
        return activityType;
    }

    public void setActivityType(ReferenceLookUp activityType) {
        this.activityType = activityType;
    }

    public Long getActivityTypeId() {
        return activityTypeId;
    }

    public void setActivityTypeId(Long activityTypeId) {
        this.activityTypeId = activityTypeId;
    }

    public ReferenceLookUp getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(ReferenceLookUp activityCode) {
        this.activityCode = activityCode;
    }

    public Long getActivityCodeId() {
        return activityCodeId;
    }

    public void setActivityCodeId(Long activityCodeId) {
        this.activityCodeId = activityCodeId;
    }

    public String getDisplayAmount() {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

        symbols.setGroupingSeparator(',');
        formatter.setDecimalFormatSymbols(symbols);
        return "P"+formatter.format(this.amount);
    }
}
