package com.smartlibrary.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation to map Java code elements directly to Architectural Concepts.
 * Demonstrates Custom Annotation definition and runtime reflection retention.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR})
public @interface CourseConcept {
    int unit();
    String concept();
    String description() default "";
}
