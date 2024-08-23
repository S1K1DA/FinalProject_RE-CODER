package com.heartlink.common.paging;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageInfo {
    private int currentPage;
    private int listCount;
    private int pageLimit;
    private int maxPage;
    private int startPage;
    private int endPage;
    private int boardLimit;
    private int startRow;
    private int endRow;

}
