package com.techcorp.internaltoolsapi.domain.analytics.dto.response.category;

/**
 * Category analytics insights.
 */
public class CategoryInsightsResponse {

    // ---------- ATTRIBUTES ---------- //

    private String mostExpensiveCategory;

    private String mostEfficientCategory;

    // ---------- CONSTRUCTORS ---------- //

    public CategoryInsightsResponse() {
    }

    public CategoryInsightsResponse(
            String mostExpensiveCategory,
            String mostEfficientCategory
    ) {

        this.mostExpensiveCategory =
                mostExpensiveCategory;

        this.mostEfficientCategory =
                mostEfficientCategory;
    }

    // ---------- GETTERS & SETTERS ---------- //

    public String getMostExpensiveCategory() {
        return mostExpensiveCategory;
    }

    public void setMostExpensiveCategory(
            String mostExpensiveCategory
    ) {
        this.mostExpensiveCategory =
                mostExpensiveCategory;
    }

    public String getMostEfficientCategory() {
        return mostEfficientCategory;
    }

    public void setMostEfficientCategory(
            String mostEfficientCategory
    ) {
        this.mostEfficientCategory =
                mostEfficientCategory;
    }

    // ---------- TO STRING ---------- //

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CategoryInsightsResponse{");
        sb.append("mostExpensiveCategory='").append(mostExpensiveCategory).append('\'');
        sb.append(", mostEfficientCategory='").append(mostEfficientCategory).append('\'');
        sb.append('}');
        return sb.toString();
    }
}