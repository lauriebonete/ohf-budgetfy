package org.ohf.bean.DTO;

import java.util.List;

/**
 * Created by Laurie on 31 Oct 2016.
 */
public class DisbursementHeaderHelper {
    private Long programId;
    private Integer columnIndex;
    private Integer startIndex;
    private Integer endIndex;
    private List<DisbursementActivityHelper> disbursementActivityHelperList;

    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    public Integer getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(Integer columnIndex) {
        this.columnIndex = columnIndex;
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    public Integer getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(Integer endIndex) {
        this.endIndex = endIndex;
    }

    public List<DisbursementActivityHelper> getDisbursementActivityHelperList() {
        return disbursementActivityHelperList;
    }

    public void setDisbursementActivityHelperList(List<DisbursementActivityHelper> disbursementActivityHelperList) {
        this.disbursementActivityHelperList = disbursementActivityHelperList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DisbursementHeaderHelper that = (DisbursementHeaderHelper) o;

        return !(programId != null ? !programId.equals(that.programId) : that.programId != null);

    }

    @Override
    public int hashCode() {
        return programId != null ? programId.hashCode() : 0;
    }
}
