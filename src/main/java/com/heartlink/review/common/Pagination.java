package com.heartlink.review.common;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Pagination {

    public Map<String, Object> getPaginatedData(int page, int pageSize, List<?> fullDataList) {
        int totalItems = fullDataList.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        int offset = (page - 1) * pageSize;

        List<?> paginatedList = fullDataList.subList(
                Math.min(offset, totalItems),
                Math.min(offset + pageSize, totalItems)
        );

        Map<String, Object> paginationData = new HashMap<>();
        paginationData.put("currentPage", page);
        paginationData.put("totalPages", totalPages);
        paginationData.put("items", paginatedList);

        return paginationData;
    }
}