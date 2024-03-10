package ru.vvi.testtask.repositories;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vvi.testtask.models.Person;

import java.awt.print.Pageable;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    Optional<Person> findByUsername(String username);

    List<Person> findByDateOfBirthGreaterThan(Date dateOfBirth);

    List<Person> findWithSortByDateOfBirthGreaterThan(Date dateOfBirth, Sort sort);
    List<Person> findWithPaginationByDateOfBirthGreaterThan(Date dateOfBirth, PageRequest page);

    List<Person> findByNameLike(String name);

    List<Person> findWithSortByNameLike(String name, Sort sort);

    List<Person> findWithPaginationByNameLike(String name, PageRequest page);

    List<Person> findAllById(int id);

}
