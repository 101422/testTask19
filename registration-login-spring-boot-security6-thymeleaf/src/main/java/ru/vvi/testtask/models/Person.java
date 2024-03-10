package ru.vvi.testtask.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.List;

@Component
@Entity
@Table(name = "persons")
public class Person {

    @Id
    @Column(name = "person_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    @Column(name = "name")
    private String name;

    //@NotEmpty(message = "Date of birth should not be empty")
    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "amount_on_deposit")
    //@NotEmpty (message = "Amount should not be empty")
    @Min(-0)
    //@Max(100)
    private Double amountOnDeposit;

    public Double getStartAmountOnDeposit() {
        return startAmountOnDeposit;
    }

    public void setStartAmountOnDeposit(Double startAmountOnDeposit) {
        this.startAmountOnDeposit = startAmountOnDeposit;
    }

    @Column(name = "start_amount_on_deposit")
    //@NotEmpty (message = "Amount should not be empty")
    @Min(-0)
    //@Max(100)
    private Double startAmountOnDeposit;

    @NotEmpty(message = "Username should not be empty")
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    @NotEmpty(message = "Password should not be empty")
    private String password;

    @Column(name = "role")
    private String role;

    @OneToMany(mappedBy = "person", fetch = FetchType.EAGER
  /*          cascade = CascadeType.ALL)*/)
    private List<PersonEmail> personEmails;

    @OneToMany(mappedBy = "person", fetch = FetchType.EAGER
   /*         cascade = CascadeType.ALL*/)
    private List<PersonPhoneNumber> personPhoneNumbers;

    @Transient
//    @NotEmpty(message = "Phone number should not be empty")
    private String phoneNumber;

    @Transient
//    @NotEmpty(message = "Email should not be empty")
    @Email
    private String email;


    public Person() {

    }

    public List<PersonPhoneNumber> getPersonPhoneNumbers() {
        return personPhoneNumbers;
    }

    public void setPersonPhoneNumbers(List<PersonPhoneNumber> personPhoneNumbers) {
        this.personPhoneNumbers = personPhoneNumbers;
    }
    public List<PersonEmail> getPersonEmails() {
        return personEmails;
    }

    public void setPersonEmails(List<PersonEmail> personEmails) {
        this.personEmails = personEmails;
    }
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getAmountOnDeposit() {
        return amountOnDeposit;
    }

    public void setAmountOnDeposit(Double amountOnDeposit) {
        this.amountOnDeposit = amountOnDeposit;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + username + '\'' +
                ", age=" + dateOfBirth +
                '}';
    }
}
