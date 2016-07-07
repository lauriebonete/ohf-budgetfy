package org.ohf.bean;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.evey.bean.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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

    @Temporal(TemporalType.DATE)
    @Column(name = "VC_DATE")
    private Date date;

    @Column(name = "TOTAL_AMOUNT")
    private BigDecimal totalAmount;

    @Column(name = "DESCREPANCY")
    private BigDecimal descrepancy;

    @OneToMany(mappedBy = "voucher")
    @JsonManagedReference(value = "VOUCHER")
    private List<Particular> particulars;

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
}

