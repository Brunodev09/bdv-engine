package com.bdv.api;

public enum ProjectDimensionNumber {
    twoDimensions("2D"),
    threeDimensions("3D");

    private final String description;

    ProjectDimensionNumber(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
