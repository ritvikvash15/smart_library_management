package com.smartlibrary.exception;

import com.smartlibrary.annotation.CourseConcept;

@CourseConcept(unit = 3, concept = "Exception Handling", description = "Wraps JDBC / JPA database access errors")
public class DatabaseOperationException extends LibraryException {
    public DatabaseOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
