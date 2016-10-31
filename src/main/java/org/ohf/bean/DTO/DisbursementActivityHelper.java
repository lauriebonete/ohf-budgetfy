package org.ohf.bean.DTO;

/**
 * Created by Laurie on 31 Oct 2016.
 */
public class DisbursementActivityHelper {
    private Long activityId;
    private Integer index;

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DisbursementActivityHelper that = (DisbursementActivityHelper) o;

        return !(activityId != null ? !activityId.equals(that.activityId) : that.activityId != null);

    }

    @Override
    public int hashCode() {
        return activityId != null ? activityId.hashCode() : 0;
    }
}
