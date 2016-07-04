package org.ohf.bean;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.evey.bean.BaseEntity;
import org.evey.bean.FileDetail;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by Laurie on 7/2/2016.
 */
@Entity
@Table(name = "PARTICULAR")
public class Particular extends BaseEntity {

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "EXPENSE")
    private BigDecimal expense;

    @ManyToOne
    @JoinColumn(name = "VOUCHER_ID", referencedColumnName = "ID")
    @JsonBackReference
    private Voucher voucher;

    @Column(name = "VOUCHER_ID", insertable = false, updatable = false)
    private Long voucherId;

    @ManyToOne
    @JoinColumn(name = "ACTIVITY_ID", referencedColumnName = "ID")
    @JsonBackReference
    private Activity activity;

    @Column(name = "ACTIVITY_ID", insertable = false, updatable = false)
    private Long activityId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECEIPT_ID", referencedColumnName = "ID")
    private FileDetail receipt;

    @Column(name = "RECEIPT_ID", insertable = false, updatable = false)
    private Long receiptId;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getExpense() {
        return expense;
    }

    public void setExpense(BigDecimal expense) {
        this.expense = expense;
    }

    public FileDetail getReceipt() {
        return receipt;
    }

    public void setReceipt(FileDetail receipt) {
        this.receipt = receipt;
    }

    public Long getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(Long receiptId) {
        this.receiptId = receiptId;
    }

    public Voucher getVoucher() {
        return voucher;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Long getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(Long voucherId) {
        this.voucherId = voucherId;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }
}
