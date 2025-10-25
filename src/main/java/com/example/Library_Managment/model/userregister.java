package com.example.Library_Managment.model;
import jakarta.persistence.*;
import lombok.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "users")
@Data
/*@AllArgsConstructor
@NoArgsConstructor*/

public class userregister {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String firstName;

    private String lastName;
    @Column(unique = true)
    private String email;


    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<BorrowHistory> borrowHistoryList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<BorrowHistory> getBorrowHistoryList() {
        return borrowHistoryList;
    }

    public void setBorrowHistoryList(List<BorrowHistory> borrowHistoryList) {
        this.borrowHistoryList = borrowHistoryList;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



}








