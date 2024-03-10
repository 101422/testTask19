package ru.vvi.testtask.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ru.vvi.testtask.models.Person;

@Component
public class PersonPasswordEncrypt {

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private Person person;

    public PersonPasswordEncrypt(Person person) {
        this.person = person;
    }

}
