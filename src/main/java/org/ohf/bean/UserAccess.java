package org.ohf.bean;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.evey.bean.BaseEntity;
import org.evey.bean.User;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Laurie on 7/2/2016.
 */
@Entity
@Table(name = "USER_ACCESS")
public class UserAccess extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private User user;

    @Column(name = "USER_ID", insertable = false, updatable = false)
    private Long userId;

    /*@OneToMany(mappedBy = "userAccess", fetch = FetchType.EAGER)
    @JsonManagedReference*/
    private transient Set<ProgramAccess> programAccessSet;

    @ManyToOne
    @JoinColumn(name = "PROGRAM_ID", referencedColumnName = "ID")
    @JsonBackReference
    private Program program;

    @Column(name = "PROGRAM_ID", insertable = false, updatable = false)
    private Long programId;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<ProgramAccess> getProgramAccessSet() {
        return programAccessSet;
    }

    public void setProgramAccessSet(Set<ProgramAccess> programAccessSet) {
        this.programAccessSet = programAccessSet;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }
}
