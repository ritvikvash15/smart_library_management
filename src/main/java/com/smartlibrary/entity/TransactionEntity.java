package com.smartlibrary.entity;

import com.smartlibrary.annotation.CourseConcept;
import com.smartlibrary.model.TransactionType;
import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * JPA Entity representing Transactions with ORM relationships (@ManyToOne).
 * Demonstrates Unit 5: Entity Relationships and JPQL Queries.
 */
@Entity
@Table(name = "transactions")
@CourseConcept(unit = 5, concept = "JPA Relationships & ORM")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id", nullable = false)
    private BookEntity book;

    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(name = "fine_amount")
    private double fineAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;

    @Column(name = "returned")
    private boolean returned;

    public TransactionEntity() {}

    public TransactionEntity(UserEntity user, BookEntity book, LocalDate issueDate, LocalDate dueDate, TransactionType transactionType) {
        this.user = user;
        this.book = book;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.transactionType = transactionType;
        this.fineAmount = 0.0;
        this.returned = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public BookEntity getBook() {
        return book;
    }

    public void setBook(BookEntity book) {
        this.book = book;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    @Override
    public String toString() {
        return String.format("TransactionEntity[ID=%d, User='%s', Book='%s', Returned=%b]",
                id, user != null ? user.getName() : "N/A", book != null ? book.getTitle() : "N/A", returned);
    }
}
