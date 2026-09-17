package com.smartlibrary.interface_contract;

import com.smartlibrary.annotation.CourseConcept;

@CourseConcept(unit = 4, concept = "Interface & I/O", description = "Interface for report generation contract")
public interface Reportable {
    String generateSummaryReport();
}
