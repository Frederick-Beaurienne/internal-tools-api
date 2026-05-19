package com.techcorp.internaltoolsapi.domain.tools.dto.response;

import com.techcorp.internaltoolsapi.domain.tools.dto.response.metadata.PaginationMetadata;
import com.techcorp.internaltoolsapi.domain.tools.dto.response.metadata.SortingMetadata;

import java.util.List;
import java.util.Map;

/**
 * Paginated response wrapper for tool search endpoints.
 * <p>
 * This DTO provides:
 * - filtered paginated data
 * - total dataset size
 * - filtered dataset size
 * - applied filters metadata
 * - pagination metadata
 * <p>
 * A dedicated DTO is intentionally used instead of directly
 * serializing Spring Page objects in order to:
 * - provide a stable API contract
 * - avoid framework-specific JSON structures
 * - fully control exposed pagination metadata
 * - align the response format with exercise requirements
 */
public class PaginatedToolResponse {

    // ---------- ATTRIBUTES ---------- //

    private List<ToolResponse> data;

    private long total;

    private long filtered;

    private Map<String, Object> filtersApplied;

    private PaginationMetadata pagination;

    private SortingMetadata sorting;

    // ---------- CONSTRUCTORS ---------- //

    public PaginatedToolResponse() {
    }

    public PaginatedToolResponse(
            List<ToolResponse> data,
            long total,
            long filtered,
            Map<String, Object> filtersApplied,
            PaginationMetadata pagination,
            SortingMetadata sorting
    ) {
        this.data = data;
        this.total = total;
        this.filtered = filtered;
        this.filtersApplied = filtersApplied;
        this.pagination = pagination;
        this.sorting = sorting;
    }

    // ---------- GETTERS & SETTERS ---------- //

    public List<ToolResponse> getData() {
        return data;
    }

    public void setData(List<ToolResponse> data) {
        this.data = data;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getFiltered() {
        return filtered;
    }

    public void setFiltered(long filtered) {
        this.filtered = filtered;
    }

    public Map<String, Object> getFiltersApplied() {
        return filtersApplied;
    }

    public void setFiltersApplied(Map<String, Object> filtersApplied) {
        this.filtersApplied = filtersApplied;
    }

    public PaginationMetadata getPagination() {
        return pagination;
    }

    public void setPagination(PaginationMetadata pagination) {
        this.pagination = pagination;
    }

    public SortingMetadata getSorting() {
        return sorting;
    }

    public void setSorting(SortingMetadata sorting) {
        this.sorting = sorting;
    }

    // ---------- TO STRING ---------- //

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PaginatedToolResponse{");
        sb.append("data=").append(data);
        sb.append(", total=").append(total);
        sb.append(", filtered=").append(filtered);
        sb.append(", filtersApplied=").append(filtersApplied);
        sb.append(", pagination=").append(pagination);
        sb.append(", sorting=").append(sorting);
        sb.append('}');
        return sb.toString();
    }
}