package com.ben.content_distribution_api.dto;

import java.util.List;

/**
 * API-facing pagination wrapper.
 *
 * Prevents exposing Spring's Page implementation directly and keeps collection
 * responses stable for clients.
 *
 * @param <T> the DTO type contained in the response data list
 */
public class PagedResponse<T> {

	private List<T> data;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    public PagedResponse(
            List<T> data,
            int page,
            int size,
            long totalElements,
            int totalPages) {

        this.data = data;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public List<T> getData() {
        return data;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
