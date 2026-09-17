package com.smartlibrary.interface_contract;

import com.smartlibrary.annotation.CourseConcept;

@CourseConcept(unit = 4, concept = "Interface & I/O", description = "Interface for formatting entities into exportable rows")
public interface Exportable {
    String toCsvRow();
    String toFormattedText();
}
