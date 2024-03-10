package ru.vvi.testtask.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.vvi.testtask.models.Person;
import ru.vvi.testtask.services.PersonDetailsService;


@Component
public class PersonAmountOnDepositValidator implements Validator {

    private final PersonDetailsService personDetailsService;

    @Autowired
    public PersonAmountOnDepositValidator(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;

        if (person.getAmountOnDeposit() < 0)
            errors.rejectValue("amountOnDeposit", "", "Сумма на счете не может быть отрицательной");
    }

}
