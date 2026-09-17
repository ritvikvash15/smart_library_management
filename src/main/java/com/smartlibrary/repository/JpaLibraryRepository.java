package com.smartlibrary.repository;

import com.smartlibrary.annotation.CourseConcept;
import com.smartlibrary.entity.BookEntity;
import com.smartlibrary.entity.TransactionEntity;
import com.smartlibrary.entity.UserEntity;
import com.smartlibrary.model.BookCategory;
import com.smartlibrary.model.BookStatus;
import com.smartlibrary.model.UserRole;
import com.smartlibrary.util.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.Collections;
import java.util.List;

/**
 * JPA Repository demonstrating complete Object-Relational Mapping (ORM) and JPQL Queries.
 * Demonstrates Unit 5: JPA EntityManager, JPQL, Transactions, Entity lifecycle.
 */
@CourseConcept(unit = 5, concept = "JPA ORM & JPQL Queries")
public class JpaLibraryRepository {

    private final EntityManagerFactory emf;

    public JpaLibraryRepository() {
        this.emf = JpaUtil.getEntityManagerFactory();
    }

    public void runJpaDemonstration() {
        if (emf == null) {
            System.out.println("⚠️ JPA Environment unavailable. Skipping demonstration.");
            return;
        }

        System.out.println("\n========================================");
        System.out.println("     JPA / HIBERNATE EXECUTABLE DEMO    ");
        System.out.println("========================================");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // 1. Create and Persist new User via JPA
            UserEntity user = new UserEntity("JPA Demo Student", "jpademo@university.edu", "9998887770", UserRole.STUDENT, "21JPA001", "Computer Science");
            em.persist(user);
            System.out.println("✅ JPA Persisted User: " + user);

            // 2. Create and Persist new Book via JPA
            BookEntity book = new BookEntity("978-0131103627", "Hibernate & JPA in Action", "Gavin King", BookCategory.COMPUTER_SCIENCE, BookStatus.AVAILABLE, "Physical Book", 5, "Rack JPA-1");
            em.persist(book);
            System.out.println("✅ JPA Persisted Book: " + book);

            tx.commit();

            // 3. Execute JPQL Query: Find Available Books
            System.out.println("\n🔍 Executing JPQL Query: Find Available Books...");
            TypedQuery<BookEntity> query = em.createQuery("SELECT b FROM BookEntity b WHERE b.status = :status", BookEntity.class);
            query.setParameter("status", BookStatus.AVAILABLE);
            List<BookEntity> availableBooks = query.getResultList();

            for (BookEntity b : availableBooks) {
                System.out.println("   -> JPQL Result: " + b.getTitle() + " | Author: " + b.getAuthor());
            }

            // 4. Execute JPQL Query: Find Users by Department
            System.out.println("\n🔍 Executing JPQL Query: Find Students by Department...");
            TypedQuery<UserEntity> userQuery = em.createQuery("SELECT u FROM UserEntity u WHERE u.department = :dept", UserEntity.class);
            userQuery.setParameter("dept", "Computer Science");
            List<UserEntity> csStudents = userQuery.getResultList();

            for (UserEntity u : csStudents) {
                System.out.println("   -> JPQL Result Student: " + u.getName() + " (" + u.getRegistrationNumber() + ")");
            }

            System.out.println("\n✅ JPA / Hibernate Operations Completed Successfully!");

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            System.err.println("❌ JPA Transaction Error: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public List<BookEntity> findAvailableBooksJpa() {
        if (emf == null) return Collections.emptyList();
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<BookEntity> query = em.createQuery("SELECT b FROM BookEntity b WHERE b.status = :status", BookEntity.class);
            query.setParameter("status", BookStatus.AVAILABLE);
            return query.getResultList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
