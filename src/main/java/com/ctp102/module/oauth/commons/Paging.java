package com.ctp102.module.oauth.commons;

import lombok.Data;

@Data
public class Paging {

    private int pageNo;
    private int pageSize;
    private long totalCount;

    public Paging() {
        this.pageNo = 1;
        this.pageSize = 20;
    }

    public Paging(int pageNo, int pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public boolean isHasMore() {
        if (this.pageNo > 0 && this.pageSize > 0 && this.totalCount > 0L) {
            return this.pageNo < (int)(this.totalCount + (long)(this.pageSize - 1)) / (long)this.pageSize;
        } else {
            return false;
        }
    }

}
