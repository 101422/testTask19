package ru.vvi.testtask.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.vvi.testtask.models.Person;
import ru.vvi.testtask.services.PersonService;

@Component
public class MoneyTransfer {

    @Autowired
    private final PersonService personService;

    public MoneyTransfer(PersonService personService) {
        this.personService = personService;
    }

    public void moneyTransfer(int personTransferFromId, int personTransferToId, Double amountToTransfer) {
        Person foundPersonTransferFrom  = personService.findOne(personTransferFromId);
        Person foundPersonTransferTo    = personService.findOne(personTransferToId);

        Double newAmountOnDepositOfDonor = foundPersonTransferFrom.getAmountOnDeposit() - amountToTransfer;
        Double newAmountOnDepositOfAcceptor = foundPersonTransferTo.getAmountOnDeposit() + amountToTransfer;


        foundPersonTransferFrom.setAmountOnDeposit(newAmountOnDepositOfDonor);
        foundPersonTransferFrom.setEmail(foundPersonTransferFrom.getPersonEmails().get(0).getEmail());
        foundPersonTransferFrom.setPhoneNumber(foundPersonTransferFrom.getPersonPhoneNumbers().get(0).getPhoneNumber());

        foundPersonTransferTo.setAmountOnDeposit(newAmountOnDepositOfAcceptor);
        foundPersonTransferTo.setEmail(foundPersonTransferTo.getPersonEmails().get(0).getEmail());
        foundPersonTransferTo.setPhoneNumber(foundPersonTransferTo.getPersonPhoneNumbers().get(0).getPhoneNumber());

        personService.update(personTransferFromId, foundPersonTransferFrom);
        personService.update(personTransferToId, foundPersonTransferTo);


    }

    public Double getNewAmountOnDepositOfDonor(int personTransferFromId, int personTransferToId, Double amountToTransfer) {
        Person foundPersonTransferFrom  = personService.findOne(personTransferFromId);
        Person foundPersonTransferTo    = personService.findOne(personTransferToId);

        Double newAmountOnDepositOfDonor = foundPersonTransferFrom.getAmountOnDeposit() - amountToTransfer;

        return newAmountOnDepositOfDonor;

    }

}
