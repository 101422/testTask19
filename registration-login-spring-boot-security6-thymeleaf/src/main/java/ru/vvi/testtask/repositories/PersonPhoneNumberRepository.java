package ru.vvi.testtask.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vvi.testtask.models.Person;
import ru.vvi.testtask.models.PersonPhoneNumber;

import java.util.List;
import java.util.Optional;


@Repository
public interface PersonPhoneNumberRepository extends JpaRepository<PersonPhoneNumber, Integer> {

    List<PersonPhoneNumber> findAllByPerson(Person person);

    Optional<PersonPhoneNumber> findByPhoneNumber(String phoneNumber);

}
