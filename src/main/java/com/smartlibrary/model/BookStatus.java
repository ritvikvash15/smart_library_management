package com.smartlibrary.model;

import com.smartlibrary.annotation.CourseConcept;

@CourseConcept(unit = 2, concept = "Enum", description = "Enumeration for tracking book availability status")
public enum BookStatus {
    AVAILABLE,
    ISSUED,
    REFERENCE_ONLY,
    LOST
}
