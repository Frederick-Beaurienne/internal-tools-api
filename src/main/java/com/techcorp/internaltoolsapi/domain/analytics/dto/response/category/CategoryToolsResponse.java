package com.techcorp.internaltoolsapi.domain.analytics.dto.response.category;

import java.util.List;

/**
 * Category analytics response.
 */
public class CategoryToolsResponse {

    // ---------- ATTRIBUTES ---------- //

    private List<CategoryResponse> data;

    private String message;

    private CategoryInsightsResponse insights;

    // ---------- CONSTRUCTORS ---------- //

    public CategoryToolsResponse() {
    }

    public CategoryToolsResponse(
            List<CategoryResponse> data,
            CategoryInsightsResponse insights
    ) {

        this.data = data;
        this.insights = insights;
    }

    public CategoryToolsResponse(
            List<CategoryResponse> data,
            String message,
            CategoryInsightsResponse insights
    ) {

        this.data = data;
        this.message = message;
        this.insights = insights;
    }

    // ---------- GETTERS & SETTERS ---------- //

    public List<CategoryResponse> getData() {
        return data;
    }

    public void setData(
            List<CategoryResponse> data
    ) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(
            String message
    ) {
        this.message = message;
    }

    public CategoryInsightsResponse getInsights() {
        return insights;
    }

    public void setInsights(
            CategoryInsightsResponse insights
    ) {
        this.insights = insights;
    }

    // ---------- TO STRING ---------- //

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CategoryToolsResponse{");
        sb.append("data=").append(data);
        sb.append(", message='").append(message).append('\'');
        sb.append(", insights=").append(insights);
        sb.append('}');
        return sb.toString();
    }
}