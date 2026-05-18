package com.techcorp.internaltoolsapi.dto.response.metadata;

/**
 * Sorting metadata for paginated API responses.
 */
public class SortingMetadata {

    // ---------- ATTRIBUTES ---------- //

    private String sortBy;

    private String direction;

    // ---------- CONSTRUCTORS ---------- //

    public SortingMetadata() {
    }

    public SortingMetadata(
            String sortBy,
            String direction
    ) {
        this.sortBy = sortBy;
        this.direction = direction;
    }

    // ---------- GETTERS & SETTERS ---------- //

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    // ---------- TO STRING ---------- //


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SortingMetadata{");
        sb.append("sortBy='").append(sortBy).append('\'');
        sb.append(", direction='").append(direction).append('\'');
        sb.append('}');
        return sb.toString();
    }
}