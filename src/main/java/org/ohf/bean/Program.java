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
 * Created by Laurie on 7/2/2016.
 */
@Entity
@Table(name = "PROGRAM")
public class Program extends BaseEntity {

    @Column(name = "PROGRAM_NAME")
    private String programName;

    @Column(name = "TOTAL_BUDGET")
    private BigDecimal totalBudget;

    @Column(name = "THRESHOLD")
    private BigDecimal threshold;

    @Column(name="PERCENTAGE")
    private Double percentage;

    @Temporal(TemporalType.DATE)
    @Column(name = "PROGRAM_START")
    private Date programStart;

    @Temporal(TemporalType.DATE)
    @Column(name = "PROGRAM_END")
    private Date programEnd;

    @OneToMany(mappedBy = "program")
    @JsonManagedReference
    private List<UserAccess> userAccessList;

    @OneToMany(mappedBy = "program")
    @JsonManagedReference
    private List<Activity> activities;

    @Column(name = "YEAR")
    private String year;

    @Column(name = "HEX_COLOR")
    private String hexColor;

    public String getHexColor() {
        return hexColor;
    }

    public void setHexColor(String hexColor) {
        this.hexColor = hexColor;
    }

    private transient String displayProgramDuration;

    private transient String displayProgramBudget;

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public BigDecimal getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(BigDecimal totalBudget) {
        this.totalBudget = totalBudget;
    }

    public BigDecimal getThreshold() {
        return threshold;
    }

    public void setThreshold(BigDecimal threshold) {
        this.threshold = threshold;
    }

    public Date getProgramStart() {
        return programStart;
    }

    public void setProgramStart(Date programStart) {
        this.programStart = programStart;
    }

    public Date getProgramEnd() {
        return programEnd;
    }

    public void setProgramEnd(Date programEnd) {
        this.programEnd = programEnd;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public List<UserAccess> getUserAccessList() {
        return userAccessList;
    }

    public void setUserAccessList(List<UserAccess> userAccessList) {
        this.userAccessList = userAccessList;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDisplayProgramDuration() {
        StringBuilder displayBuilder = new StringBuilder();
        if(this.programStart!=null
                && this.programEnd !=null){
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");
            displayBuilder.append(dateFormat.format(this.programStart))
                    .append(" - ")
                    .append(dateFormat.format(this.programEnd));
        }


        return displayBuilder.toString();
    }

    public String getDisplayProgramBudget() {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

        symbols.setGroupingSeparator(',');
        formatter.setDecimalFormatSymbols(symbols);
        return "P"+formatter.format(this.totalBudget);
    }

    @Override
    public Map<String, String> getOrderBy() {
        Map<String, String> orderMap = new HashMap<>();
        orderMap.put("id","DESC");
        return orderMap;
    }

    @Override
    protected void prePersist() {
        super.prePersist();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        this.year = dateFormat.format(this.programStart);
    }
}
