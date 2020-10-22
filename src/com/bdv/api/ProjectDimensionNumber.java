package com.bdv.api;

public enum ProjectDimensionNumber {
    TwoDimensions("2D"),
    ThreeDimensions("3D");

    private final String description;

    ProjectDimensionNumber(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
