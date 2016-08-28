package org.ohf.bean.DTO;

import java.math.BigDecimal;

/**
 * Created by Laurie on 8/28/2016.
 */
public class DisbursementDTO {

    private Integer row;
    private Long voucherId;
    private String voucherDate;
    private String payee;
    private String particulars;
    private String reference;
    private String vcNumber;
    private BigDecimal totalAmount;

    public Long getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(Long voucherId) {
        this.voucherId = voucherId;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getVcNumber() {
        return vcNumber;
    }

    public void setVcNumber(String vcNumber) {
        this.vcNumber = vcNumber;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }
}
