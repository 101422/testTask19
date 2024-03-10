package ru.vvi.testtask.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.stereotype.Component;

@Component
@Entity
@Table(name = "phonenumbers")
public class PersonPhoneNumber {
    @Id
    @Column(name = "phone_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "phone_number")
    @NotEmpty(message = "Phone number should not be empty")
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    public PersonPhoneNumber() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return "Phone Number{" +
                "id=" + id +
                ", PhoneNumber='" + phoneNumber +
                '}';
    }
}
