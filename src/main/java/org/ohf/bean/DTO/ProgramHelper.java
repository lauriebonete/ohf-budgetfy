package org.ohf.bean.DTO;

import org.ohf.bean.Activity;

import java.util.List;

/**
 * Created by Laurie on 24 Oct 2016.
 */
public class ProgramHelper {
    private Long programId;
    private String programName;
    private List<Activity> activityList;
    private String hexColor;

    public String getHexColor() {
        return hexColor;
    }

    public void setHexColor(String hexColor) {
        this.hexColor = hexColor;
    }

    public List<Activity> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<Activity> activityList) {
        this.activityList = activityList;
    }

    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProgramHelper that = (ProgramHelper) o;

        return !(programId != null ? !programId.equals(that.programId) : that.programId != null);

    }

    @Override
    public int hashCode() {
        return programId != null ? programId.hashCode() : 0;
    }
}
