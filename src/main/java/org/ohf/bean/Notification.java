package org.ohf.bean;

import org.evey.bean.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.text.SimpleDateFormat;

/**
 * Created by Laurie on 19 Dec 2016.
 */
@Entity
public class Notification extends BaseEntity {

    @Column(name = "MESSAGE")
    private String message;

    @Column(name = "CLASS_TYPE")
    private String classType;

    @Column(name = "CLASS_ID")
    private Long classId;

    @Column(name = "NOTIFY_USER")
    private Long notifyUserId;

    @Column(name = "IS_SEEN")
    private Boolean isSeen;

    private transient String createDateDisplay;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public Long getNotifyUserId() {
        return notifyUserId;
    }

    public void setNotifyUserId(Long notifyUserId) {
        this.notifyUserId = notifyUserId;
    }

    public Boolean getIsSeen() {
        return isSeen;
    }

    public void setIsSeen(Boolean isSeen) {
        this.isSeen = isSeen;
    }

    public String getCreateDateDisplay() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a MMM d, yyyy");
        return dateFormat.format(this.getCreateDate());
    }

    @Override
    protected void prePersist() {
        super.prePersist();
        this.setIsSeen(false);
    }
}
