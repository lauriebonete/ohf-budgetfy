package org.ohf.bean;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.evey.bean.BaseEntity;

import javax.persistence.*;

/**
 * Created by Laurie on 7/2/2016.
 */
@Entity
@Table(name = "PROGRAM_ACCESS")
public class ProgramAccess extends BaseEntity {

    @Column(name="PRIVILEGE")
    private String access;

    @ManyToOne
    @JoinColumn(name = "ACCESS_ID", referencedColumnName = "ID")
    @JsonBackReference
    private UserAccess userAccess;

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public UserAccess getUserAccess() {
        return userAccess;
    }

    public void setUserAccess(UserAccess userAccess) {
        this.userAccess = userAccess;
    }
}
