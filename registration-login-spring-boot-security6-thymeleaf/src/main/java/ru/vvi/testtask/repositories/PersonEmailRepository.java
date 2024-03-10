package ru.vvi.testtask.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vvi.testtask.models.Person;
import ru.vvi.testtask.models.PersonEmail;

import java.util.List;
import java.util.Optional;


@Repository
public interface PersonEmailRepository extends JpaRepository<PersonEmail, Integer> {
    List<PersonEmail> findAllByPerson(Person person);

    Optional<PersonEmail> findByEmail(String email);

}
