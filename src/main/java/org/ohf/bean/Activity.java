package org.ohf.bean;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.evey.bean.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

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

    @Column(name = "CODE")
    private String code;

    @ManyToOne
    @JoinColumn(name = "PROGRAM_ID", referencedColumnName = "ID")
    @JsonBackReference
    private Program program;

    @Column(name = "PROGRAM_ID", insertable = false, updatable = false)
    private Long programId;

    @OneToMany(mappedBy = "activity")
    @JsonManagedReference
    private transient List<Particular> particulars;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
}
