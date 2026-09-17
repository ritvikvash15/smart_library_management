package com.smartlibrary.model;

import com.smartlibrary.annotation.CourseConcept;

@CourseConcept(unit = 2, concept = "Enum", description = "Enumeration representing Book Categories")
public enum BookCategory {
    COMPUTER_SCIENCE("Computer Science"),
    MATHEMATICS("Mathematics"),
    PHYSICS("Physics"),
    FICTION("Fiction"),
    NON_FICTION("Non-Fiction"),
    REFERENCE("Reference Material");

    private final String displayName;

    BookCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
