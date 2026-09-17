package com.smartlibrary.util;

import com.smartlibrary.annotation.CourseConcept;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Singleton Utility class for managing JPA EntityManagerFactory lifecycle.
 * Demonstrates Unit 5: JPA Persistence Context and Singleton Pattern.
 */
@CourseConcept(unit = 5, concept = "JPA EntityManagerFactory & Singleton")
public class JpaUtil {
    private static volatile EntityManagerFactory emf;
    private static final String PERSISTENCE_UNIT_NAME = "smart-library-pu";

    private JpaUtil() {}

    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null || !emf.isOpen()) {
            synchronized (JpaUtil.class) {
                if (emf == null || !emf.isOpen()) {
                    try {
                        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
                    } catch (Exception e) {
                        System.err.println("JPA Initialization Warning: " + e.getMessage());
                    }
                }
            }
        }
        return emf;
    }

    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
