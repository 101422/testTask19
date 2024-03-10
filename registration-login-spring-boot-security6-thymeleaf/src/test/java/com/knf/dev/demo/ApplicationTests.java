package com.knf.dev.demo;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.vvi.testtask.SpringHibernateAppBootApplication;
import ru.vvi.testtask.models.Person;
import ru.vvi.testtask.services.PersonService;
import ru.vvi.testtask.util.MoneyTransfer;

import java.sql.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringHibernateAppBootApplication.class)
class ApplicationTests {

	private final PersonService personService;
	private final MoneyTransfer moneyTransfer;

	@Autowired
	ApplicationTests(PersonService personService, MoneyTransfer moneyTransfer) {
		this.personService = personService;
		this.moneyTransfer = moneyTransfer;
	}


	@Test
	void moneyTransferTest() {

		moneyTransfer.moneyTransfer(84, 83, 600.0);

	}

}
