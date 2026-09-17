package com.smartlibrary.model;

import com.smartlibrary.annotation.CourseConcept;

@CourseConcept(unit = 2, concept = "Enum", description = "Enumeration for Library Transaction Types")
public enum TransactionType {
    ISSUE,
    RETURN,
    FINE_PAYMENT
}
