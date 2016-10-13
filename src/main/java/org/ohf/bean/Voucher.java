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

    private transient String displayDate;

    private transient String displaytotalAmount;

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

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(BigDecimal totalExpense) {
        if (totalExpense==null){
            for (Particular particular : particulars) {
                this.totalExpense = this.totalExpense.add(particular.getExpense());
            }
        } else {
            this.totalExpense = totalExpense;
        }
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

    @Override
    public Map<String, String> getOrderBy() {
        Map<String, String> orderMap = new HashMap<>();
        orderMap.put("id","DESC");
        return orderMap;
    }
}

