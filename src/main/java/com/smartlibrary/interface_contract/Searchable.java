package com.smartlibrary.interface_contract;

import com.smartlibrary.annotation.CourseConcept;

@CourseConcept(unit = 2, concept = "Interface", description = "Interface for searchable entities")
public interface Searchable {
    boolean matchesKeyword(String keyword);
}
