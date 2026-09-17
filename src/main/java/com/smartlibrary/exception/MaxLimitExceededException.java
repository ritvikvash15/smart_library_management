package com.smartlibrary.exception;

import com.smartlibrary.annotation.CourseConcept;

@CourseConcept(unit = 3, concept = "Exception Handling", description = "Thrown when student attempts to borrow beyond maximum permitted book quota")
public class MaxLimitExceededException extends LibraryException {
    public MaxLimitExceededException(String message) {
        super(message);
    }
}
