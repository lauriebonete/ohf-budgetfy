package org.ohf.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.evey.bean.BaseEntity;
import org.evey.bean.ReferenceLookUp;

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
@Table(name = "VOUCHER")
public class Voucher extends BaseEntity {

    @Column(name = "VC_NUMBER")
    private String vcNumber;

    @Column(name = "REFERENCE")
    private String reference;

    @Column(name = "PAYEE")
    private String payee;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(name = "VC_DATE")
    private Date date;

    @Column(name="VOUCHER_YEAR")
    private String voucherYear;

    @Column(name = "TOTAL_AMOUNT")
    private BigDecimal totalAmount;

    @Column(name = "TOTAL_EXPENSE")
    private BigDecimal totalExpense;

    @Column(name = "DESCREPANCY")
    private BigDecimal descrepancy;

    @OneToMany(mappedBy = "voucher")
    @JsonManagedReference(value = "VOUCHER")
    private List<Particular> particulars;

    @ManyToOne
    @JoinColumn(name = "STATUS_ID", referencedColumnName = "ID")
    private ReferenceLookUp status;

    @Column(name = "STATUS_ID", insertable = false, updatable = false)
    private Long statusId;

    @Column(name = "IS_NOTIFIED")
    private Boolean isNotified;

    private transient String displayDate;

    private transient String displaytotalAmount;

    private transient String displayTotalExpensePage;

    private transient BigDecimal displayTotalExpense;

    public String getVcNumber() {
        return vcNumber;
    }

    public void setVcNumber(String vcNumber) {
        this.vcNumber = vcNumber;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getDescrepancy() {
        return descrepancy;
    }

    public void setDescrepancy(BigDecimal descrepancy) {
        this.descrepancy = descrepancy;
    }

    public List<Particular> getParticulars() {
        return particulars;
    }

    public void setParticulars(List<Particular> particulars) {
        this.particulars = particulars;
    }

    public ReferenceLookUp getStatus() {
        return status;
    }

    public String getVoucherYear() {
        return voucherYear;
    }

    public void setVoucherYear(String voucherYear) {
        this.voucherYear = voucherYear;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public BigDecimal getDisplayTotalExpense() {
        if (getTotalExpense()!=null){
            return totalAmount.subtract(getTotalExpense());
        }
        return totalAmount;
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(BigDecimal totalExpense) {
        this.totalExpense = totalExpense;
    }

    public void setStatus(ReferenceLookUp status) {
        this.status = status;
    }

    public String getDisplayDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");
        if (this.date!=null){
            return dateFormat.format(this.date);
        }
        return "";
    }

    public String getDisplaytotalAmount() {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

        symbols.setGroupingSeparator(',');
        formatter.setDecimalFormatSymbols(symbols);
        return "P"+formatter.format(this.totalAmount);
    }

    public String getDisplayTotalExpensePage() {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

        symbols.setGroupingSeparator(',');
        formatter.setDecimalFormatSymbols(symbols);
        return "P"+formatter.format(this.totalExpense);
    }

    public void setDisplayTotalExpensePage(String displayTotalExpensePage) {
        this.displayTotalExpensePage = displayTotalExpensePage;
    }

    public Boolean getIsNotified() {
        return isNotified;
    }

    public void setIsNotified(Boolean isNotified) {
        this.isNotified = isNotified;
    }

    public void setDisplayDate(String displayDate) {
        this.displayDate = displayDate;
    }

    @Override
    public Map<String, String> getOrderBy() {
        Map<String, String> orderMap = new HashMap<>();
        orderMap.put("id","DESC");
        return orderMap;
    }

    @Override
    @PrePersist
    protected void prePersist() {
        super.prePersist();
        this.isNotified = false;
        if(this.particulars!=null &&
                this.particulars.size()>0){
            BigDecimal total = new BigDecimal(0);
            for (Particular particular: particulars){
                total = total.add(particular.getExpense());
            }
            this.setTotalExpense(total);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        this.voucherYear = dateFormat.format(this.date);
    }


}

