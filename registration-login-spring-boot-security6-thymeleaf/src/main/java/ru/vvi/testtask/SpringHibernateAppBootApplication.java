package ru.vvi.testtask;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.vvi.testtask.util.BalanceIncrease;



@SpringBootApplication
@EnableScheduling
public class SpringHibernateAppBootApplication {

	@Autowired
	private BalanceIncrease balanceIncrease;

	public static void main(String[] args) {
		SpringApplication.run(SpringHibernateAppBootApplication.class, args);
	}

	@PostConstruct
	public void init() throws InterruptedException {
		balanceIncrease.balanceIncrease();
	}
}
