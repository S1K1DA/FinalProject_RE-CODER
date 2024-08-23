package com.heartlink.common.paging;

public class Pagination {

    public static PageInfo getPageInfo(int listCount, int currentPage, int pageLimit, int boardLimit) {
        PageInfo pi = new PageInfo();

        int maxPage = (int) Math.ceil((double) listCount / boardLimit);
        int startPage = ((currentPage - 1) / pageLimit) * pageLimit + 1;
        int endPage = startPage + pageLimit - 1;

        if (endPage > maxPage) {
            endPage = maxPage;
        }

        int startRow = (currentPage - 1) * boardLimit;
        int endRow = startRow + boardLimit;

        pi.setCurrentPage(currentPage);
        pi.setListCount(listCount);
        pi.setPageLimit(pageLimit);
        pi.setMaxPage(maxPage);
        pi.setStartPage(startPage);
        pi.setEndPage(endPage);
        pi.setBoardLimit(boardLimit);
        pi.setStartRow(startRow);
        pi.setEndRow(endRow);

        return pi;
    }
}
