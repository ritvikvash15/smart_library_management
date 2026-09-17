package com.smartlibrary.interface_contract;

import com.smartlibrary.annotation.CourseConcept;

@CourseConcept(unit = 2, concept = "Interface", description = "Interface for items that can be checked out")
public interface Borrowable {
    boolean isBorrowable();
    int getMaxBorrowDurationDays();
    double calculateFine(int overdueDays);
}
