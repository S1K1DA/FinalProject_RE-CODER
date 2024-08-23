package com.heartlink.review.common;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Pagination {

    public Map<String, Object> getPagination(int page, int pageSize, List<?> fullDataList) {
        int totalItems = fullDataList.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        // 페이지 수가 0보다 작은 경우, 최소 1로 설정
        if (totalPages < 1) {
            totalPages = 1;
        }

        int offset = (page - 1) * pageSize;

        List<?> paginatedList = fullDataList.subList(
                Math.min(offset, totalItems),
                Math.min(offset + pageSize, totalItems)
        );

        int maxPageButtons = 5; // 표시할 최대 페이지 버튼 수
        int startPage = Math.max(1, page - maxPageButtons / 2);
        int endPage = Math.min(totalPages, startPage + maxPageButtons - 1);

        if (endPage - startPage < maxPageButtons - 1) {
            startPage = Math.max(1, endPage - maxPageButtons + 1);
        }

        Map<String, Object> paginationData = new HashMap<>();
        paginationData.put("currentPage", page);
        paginationData.put("totalPages", totalPages);
        paginationData.put("startPage", startPage);
        paginationData.put("endPage", endPage);
        paginationData.put("items", paginatedList);

        return paginationData;
    }
}
