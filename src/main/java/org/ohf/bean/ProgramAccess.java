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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ProgramAccess that = (ProgramAccess) o;

        if (access != null ? !access.equals(that.access) : that.access != null) return false;
        return !(userAccess != null ? !userAccess.equals(that.userAccess) : that.userAccess != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (access != null ? access.hashCode() : 0);
        result = 31 * result + (userAccess != null ? userAccess.hashCode() : 0);
        return result;
    }
}
