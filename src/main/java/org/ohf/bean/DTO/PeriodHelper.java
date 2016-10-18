package org.ohf.bean.DTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Laurie on 18 Oct 2016.
 */
public class PeriodHelper {
    private Long vcId;
    private String vcDate;
    private String payee;
    private String particulars;
    private String reference;
    private String vcNumber;
    private BigDecimal totalExpense;
    private List<DisbursementDTO> disbursementDTOList;

    public Long getVcId() {
        return vcId;
    }

    public void setVcId(Long vcId) {
        this.vcId = vcId;
    }

    public String getVcDate() {
        return vcDate;
    }

    public void setVcDate(String vcDate) {
        this.vcDate = vcDate;
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

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(BigDecimal totalExpense) {
        this.totalExpense = totalExpense;
    }

    public List<DisbursementDTO> getDisbursementDTOList() {
        return disbursementDTOList;
    }

    public void setDisbursementDTOList(List<DisbursementDTO> disbursementDTOList) {
        this.disbursementDTOList = disbursementDTOList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PeriodHelper that = (PeriodHelper) o;

        return !(vcId != null ? !vcId.equals(that.vcId) : that.vcId != null);

    }

    @Override
    public int hashCode() {
        return vcId != null ? vcId.hashCode() : 0;
    }
}
