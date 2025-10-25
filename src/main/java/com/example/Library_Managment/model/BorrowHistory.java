
package com.example.Library_Managment.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
@Entity
@Data
public class BorrowHistory {
        
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public userregister getUser() {
                return user;
        }

        public void setUser(userregister user) {
                this.user = user;
        }

        public Book getBook() {
                return book;
        }

        public void setBook(Book book) {
                this.book = book;
        }

        public LocalDate getBorrowDate() {
                return borrowDate;
        }

        public void setBorrowDate(LocalDate borrowDate) {
                this.borrowDate = borrowDate;
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

        public Status getStatus() {
                return status;
        }

        public void setStatus(Status status) {
                this.status = status;
        }

        @ManyToOne
        @JoinColumn(name = "user_id")
        private userregister user;

        @ManyToOne
        @JoinColumn(name = "book_id")
        private Book book;

        private LocalDate borrowDate;
        private LocalDate dueDate;
        private LocalDate returnDate;

        @Enumerated(EnumType.STRING)
        private Status status;

        public enum Status {
            BORROWED, RETURNED
        }
    }


