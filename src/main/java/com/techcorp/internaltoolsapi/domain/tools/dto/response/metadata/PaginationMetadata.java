package com.techcorp.internaltoolsapi.domain.tools.dto.response.metadata;

/**
 * Pagination metadata for paginated API responses.
 */
public class PaginationMetadata {

    // ---------- ATTRIBUTES ---------- //

    private int currentPage;

    private int pageSize;

    private int totalPages;

    private boolean first;

    private boolean last;

    // ---------- CONSTRUCTORS ---------- //

    public PaginationMetadata() {
    }

    public PaginationMetadata(
            int currentPage,
            int pageSize,
            int totalPages,
            boolean first,
            boolean last
    ) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.first = first;
        this.last = last;
    }

    // ---------- GETTERS & SETTERS ---------- //

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    // ---------- TO STRING ---------- //

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PaginationMetadata{");
        sb.append("currentPage=").append(currentPage);
        sb.append(", pageSize=").append(pageSize);
        sb.append(", totalPages=").append(totalPages);
        sb.append(", first=").append(first);
        sb.append(", last=").append(last);
        sb.append('}');
        return sb.toString();
    }
}