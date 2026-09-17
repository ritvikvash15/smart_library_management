package com.smartlibrary.model;

import com.smartlibrary.annotation.CourseConcept;

@CourseConcept(unit = 2, concept = "Enum", description = "Enumeration for User Roles")
public enum UserRole {
    STUDENT,
    LIBRARIAN,
    FACULTY
}
