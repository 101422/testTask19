package ru.vvi.testtask.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.vvi.testtask.models.Person;
import ru.vvi.testtask.models.PersonEmail;
import ru.vvi.testtask.models.PersonPhoneNumber;
import ru.vvi.testtask.services.PersonEmailService;
import ru.vvi.testtask.services.PersonPhoneNumberService;
import ru.vvi.testtask.services.PersonService;
import ru.vvi.testtask.services.RegistrationService;
import ru.vvi.testtask.util.*;

import java.security.Principal;
import java.util.ArrayList;

@Tag(name = "Persons", description = "Persons management")
@CrossOrigin(origins = "http://localhost:9090")
@Controller
@RequestMapping("/persons")
public class PersonController {

    private final PersonService personService;
    private final PersonEmailService personEmailService;
    private final PersonPhoneNumberService personPhoneNumberService;
    private final PersonEmail personEmail;
    private final PersonPhoneNumber personPhoneNumber;
    private final PersonUtil personUtil;
    private final PersonValidator personValidator;
    private final PersonEmailValidator personEmailValidator;
    private final PersonPhoneNumberValidator personPhoneNumberValidator;
    private final PersonAmountOnDepositValidator personAmountOnDepositValidator;
    private final RegistrationService registrationService;

    private final MoneyTransfer moneyTransfer;

    @Autowired
    private ApplicationContext context;

    @Autowired
    public PersonController(PersonService personService, PersonEmailService personEmailService, PersonPhoneNumberService personPhoneNumberService,
                            PersonEmail personEmail, PersonPhoneNumber personPhoneNumber, PersonUtil personUtil, PersonValidator personValidator,
                            PersonEmailValidator personEmailValidator, PersonPhoneNumberValidator personPhoneNumberValidator,
                            PersonAmountOnDepositValidator personAmountOnDepositValidator, RegistrationService registrationService, MoneyTransfer moneyTransfer) {
        this.personService = personService;
        this.personEmailService = personEmailService;
        this.personPhoneNumberService = personPhoneNumberService;
        this.personEmail = personEmail;
        this.personPhoneNumber = personPhoneNumber;
        this.personUtil = personUtil;
        this.personValidator = personValidator;
        this.personEmailValidator = personEmailValidator;
        this.personPhoneNumberValidator = personPhoneNumberValidator;
        this.registrationService = registrationService;
        this.personAmountOnDepositValidator = personAmountOnDepositValidator;
        this.moneyTransfer = moneyTransfer;
    }

    @Operation(summary = "Retrieve all persons", tags = { "persons", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Person.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "204", description = "There are no persons", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping()
    public String index(Model model) {
        model.addAttribute("persons", personService.findAll());
        return "persons/index";
    }

    @Operation(summary = "Show one persons", tags = { "persons", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Person.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "204", description = "There is no person", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", personService.findOne(id));
        return "persons/show";
    }

    @Operation(summary = "Show person create page", tags = { "persons", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Person.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {


        return "persons/new";
    }

    @Operation(summary = "Create a new person", tags = { "persons", "post" })
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {
                    @Content(schema = @Schema(implementation = Person.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {

        PersonEmail personEmail = new PersonEmail();

        PersonPhoneNumber personPhoneNumber = new PersonPhoneNumber();

        person.setStartAmountOnDeposit(person.getAmountOnDeposit());

        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors())
            return "persons/new";

        personEmail.setPerson(person);
        personEmail.setEmail(person.getEmail());

        personEmailValidator.validate(personEmail, bindingResult);

        if (bindingResult.hasErrors())
            return "persons/new";

        personPhoneNumber.setPerson(person);
        personPhoneNumber.setPhoneNumber(person.getPhoneNumber());

        personPhoneNumberValidator.validate(personPhoneNumber, bindingResult);

        if (bindingResult.hasErrors())
            return "persons/new";

        registrationService.register(person);

        personEmailService.save(personEmail);

        personPhoneNumberService.save(personPhoneNumber);

        return "redirect:/persons";
    }

    @Operation(summary = "Redirect to the person edit page after authorisation", tags = { "persons", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Person.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/authorized")
    public String authorized (Principal principal) {
       Person authorizedPerson =  personService.findOneByUsername(principal.getName());

       return "redirect:/persons/" + authorizedPerson.getId() + "/edit";
    }

    @Operation(summary = "Show person edit page", tags = { "persons", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Person.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "204", description = "There is no person", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        Person person = personService.findOne(id);

        model.addAttribute("person", person);
        model.addAttribute("personEmail", personEmail);
        model.addAttribute("personPhoneNumber", personPhoneNumber);
        model.addAttribute("personEmails", personEmailService.findAllByPerson(person));
        model.addAttribute("personPhoneNumbers", personPhoneNumberService.findAllByPerson(person));

        return "persons/edit";
    }

    @Operation(summary = "Update a person by id", tags = { "persons", "patch" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Person.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }) })
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "persons/edit";

        personService.update(id, person);

        return "redirect:/persons";
    }

    @Operation(summary = "Delete a person by id", tags = { "persons", "delete" })
    @ApiResponses({ @ApiResponse(responseCode = "204", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        personService.delete(id);

        return "redirect:/persons";
    }

    @Operation(summary = "Delete one email of a person by id", tags = { "persons", "patch" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Person.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }) })
    @PatchMapping("/{id}/{emailId}/deleteEmail")
    public String deleteEmail(@PathVariable("id") int id, @PathVariable("emailId") int emailId) {
        Person person = personService.findOne(id);

        PersonEmail personEmail = personEmailService.findOne(emailId);

        personUtil.deleteEmail(personEmail);

        return "redirect:/login";
    }

    @Operation(summary = "Delete one phone number of a person by id", tags = { "persons", "patch" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Person.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }) })
    @PatchMapping("/{id}/{phoneNumberId}/deletePhoneNumber")
    public String deletePhoneNumber(@PathVariable("id") int id, @PathVariable("phoneNumberId") int phoneNumberId) {
        Person person = personService.findOne(id);

        PersonPhoneNumber personPhoneNumber = personPhoneNumberService.findOne(phoneNumberId);

        personUtil.deletePhoneNumber(personPhoneNumber);

        return "redirect:/login";
    }

    @Operation(summary = "Add one email of a person by id", tags = { "persons", "patch" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Person.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }) })
    @PostMapping("{id}/addEmail")
    public String addEmail(@ModelAttribute("personEmail") @Valid PersonEmail personEmail, BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "redirect:/login";

        Person foundPerson = personService.findOne(id);

        personEmail.setPerson(foundPerson);

        personEmailService.save(personEmail);

        return "redirect:/login";
    }

    @Operation(summary = "Add one phone number of a person by id", tags = { "persons", "patch" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Person.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }) })
    @PostMapping("{id}/addPhoneNumber")
    public String addPhoneNumber(@ModelAttribute("personEmail") @Valid PersonPhoneNumber personPhoneNumber, BindingResult bindingResult, @PathVariable("id") int id) {

        if (bindingResult.hasErrors())
            return "redirect:/login";

        Person foundPperson = personService.findOne(id);

        personPhoneNumber.setPerson(foundPperson);

        personPhoneNumberService.save(personPhoneNumber);

        return "redirect:/login";
    }

    @Operation(summary = "Show a search page", tags = { "persons", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Person.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/search")
    public String searchPage() {
        return "persons/search";
    }


    @Operation(summary = "Search a person", tags = { "persons", "post" })
    @ApiResponses({
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PostMapping("/search")
    public String makeSearch(Model model,
                             @RequestParam(value ="dateOfBirthSearching", required = false) String dateOfBirthSearching,
                             @RequestParam(value ="nameSearching", required = false) String nameSearching,
                             @RequestParam(value ="emailSearching", required = false) String emailSearching,
                             @RequestParam(value ="phoneNumberSearching", required = false) String phoneNumberSearching,
                             @RequestParam(value = "page", required = false) Integer page,
                             @RequestParam(value = "personPerPage", required = false) Integer personPerPage,
                             @RequestParam(value = "sortByYear", required = false) boolean sortByYear) {

        if (!dateOfBirthSearching.equals(""))
            if (page == null || personPerPage == null)
                model.addAttribute("persons", personService.findByDateOfBirthGreaterThan(java.sql.Date.valueOf(dateOfBirthSearching), sortByYear));
            else
                model.addAttribute("persons", personService.findWithPaginationAndSortByDateOfBirthGreaterThan(java.sql.Date.valueOf(dateOfBirthSearching), page, personPerPage, sortByYear));

        if (!nameSearching.equals(""))
            if (page == null || personPerPage == null)
                model.addAttribute("persons", personService.findByNameLike("%" + nameSearching + "%", sortByYear));
            else
                model.addAttribute("persons", personService.findWithPaginationAndSortByNameLike("%" + nameSearching + "%", page, personPerPage, sortByYear));

        PersonEmail foundEmail = personEmailService.findOneByEmail(emailSearching);

        if (!emailSearching.equals(""))
            if (foundEmail == null)
                model.addAttribute("persons", new ArrayList<>()); // пустой список
            else
                model.addAttribute("persons", personService.findAllById(foundEmail.getPerson().getId()));

        PersonPhoneNumber foundPhoneNumber = personPhoneNumberService.findOneByPhoneNumber(phoneNumberSearching);

        if (!phoneNumberSearching.equals(""))
            if (foundPhoneNumber == null)
                model.addAttribute("persons", new ArrayList<>()); // пустой список
            else
                model.addAttribute("persons", personService.findAllById(foundPhoneNumber.getPerson().getId()));

        return "persons/search";
    }

/*    @Operation(summary = "Open a transfer page", tags = { "persons", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Person.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/transfer")
    public String transferPage(Model model, @ModelAttribute("person") Person person) {

        return "persons/transfer";
    }*/
    @Operation(summary = "Show a person money transfer page", tags = { "persons", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Person.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "204", description = "There is no person", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping("{id}/transfer")
    public String transferPage(Model model, @ModelAttribute("person") Person person, @PathVariable("id") int id) {
        model.addAttribute("personTransferFrom", personService.findOne(id));
        model.addAttribute("personsTransferTo", personService.findAll());

        return "persons/transfer";
    }

    @Operation(summary = "Transfer money of a person by id", tags = { "persons", "post" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Person.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }) })
    @PostMapping("{id}/transfer")
    public String makeTransfer(Model model,
                               @ModelAttribute("person") Person person,
                               /*@RequestParam(value ="personTransferFromId", required = false) int personTransferFromId,*/
                               @RequestParam(value ="personTransferToId", required = false) int personTransferToId,
                             @RequestParam(value ="amountToTransfer", required = false) Double amountOnDeposit,
                               @PathVariable("id") int id,
                               BindingResult bindingResult
 ) {

        Double amountToTransfer = person.getAmountOnDeposit();

        Double newAmountOnDepositOfDonor = moneyTransfer.getNewAmountOnDepositOfDonor(id, personTransferToId, amountToTransfer);

        Person foundPersonTransferFrom = personService.findOne(id);
        Person foundPersonTransferTo = personService.findOne(personTransferToId);


        person.setDateOfBirth(foundPersonTransferFrom.getDateOfBirth());
        person.setUsername(foundPersonTransferFrom.getUsername());
        person.setPassword(foundPersonTransferFrom.getPassword());
        person.setRole(foundPersonTransferFrom.getRole());
        person.setName(foundPersonTransferFrom.getName());
        
        person.setAmountOnDeposit(newAmountOnDepositOfDonor);

        personAmountOnDepositValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors())
            return "/persons/transfer";

        moneyTransfer.moneyTransfer(id, personTransferToId, amountToTransfer);

        return "redirect:/persons/" + id + "/transfer";
    }
}
