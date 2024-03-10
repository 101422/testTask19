package ru.vvi.testtask.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ru.vvi.testtask.models.Person;
import ru.vvi.testtask.services.PersonService;
import java.util.Iterator;

@Component
public class BalanceIncrease
{
    @Autowired
    PersonService personService;

    public BalanceIncrease(PersonService personService) {
        this.personService = personService;
    }
    @Async("threadPoolTaskExecutor")
    public void balanceIncrease() throws InterruptedException {
        while (true) {

            Thread.sleep(60000);

            System.out.println("Async task");

            personService.findAll();

            Iterator<Person> iterator = personService.findAll().iterator();

            while (iterator.hasNext()) {
                Person person = iterator.next();

                Double amountOnDeposit = person.getAmountOnDeposit();
                Double startAmountOnDeposit = person.getStartAmountOnDeposit();

                if (amountOnDeposit < startAmountOnDeposit * 2.07) {
                    person.setAmountOnDeposit(amountOnDeposit * 1.05);
                    person.setEmail(person.getPersonEmails().get(0).getEmail());
                    person.setPhoneNumber(person.getPersonPhoneNumbers().get(0).getPhoneNumber());

                    System.out.println("Async task");

                    personService.update(person.getId(), person);
                }
            }
        }
    }

}
