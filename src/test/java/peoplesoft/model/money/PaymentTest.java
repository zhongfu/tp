package peoplesoft.model.money;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static peoplesoft.testutil.Assert.assertThrows;
import static peoplesoft.testutil.TypicalPersons.ALICE;
import static peoplesoft.testutil.TypicalPersons.BENSON;
import static peoplesoft.testutil.TypicalPersons.CARL;
import static peoplesoft.testutil.TypicalPersons.getTypicalAddressBook;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import peoplesoft.model.AddressBook;
import peoplesoft.model.Model;
import peoplesoft.model.ModelManager;
import peoplesoft.model.UserPrefs;
import peoplesoft.model.employment.Employment;
import peoplesoft.model.job.Job;
import peoplesoft.model.money.exceptions.PaymentRequiresPersonException;
import peoplesoft.model.person.Person;
import peoplesoft.model.util.ID;
import peoplesoft.testutil.PersonBuilder;


public class PaymentTest {

    private static final Job EATING = new Job(new ID(1043), "Eating", Duration.ofDays(1));
    private static final Job RUNNING = new Job(new ID(3175), "Running", Duration.ofHours(8));

    @Test
    public void createPendingPayments_noPersonAssigned_throwsPaymentRequiresPersonException() {
        Employment employment = new Employment();
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        assertThrows(PaymentRequiresPersonException.class, () ->
                PaymentHandler.createPendingPayments(EATING, model, employment));
    }

    @Test
    public void createPendingPayments_singlePerson() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Employment employment = new Employment();
        Person newAlice = new PersonBuilder(ALICE).build();

        employment.associate(EATING, newAlice);

        Map<ID, Payment> alicePayments = new HashMap<>(newAlice.getPayments());

        PaymentHandler.createPendingPayments(EATING, model, employment);

        Payment newAlicePayment = Payment.createPayment(newAlice, EATING, EATING.calculatePay(newAlice.getRate()));

        alicePayments.put(EATING.getJobId(), newAlicePayment);

        assertEquals(alicePayments, model.getPerson(newAlice.getPersonId()).getPayments());
    }

    @Test
    public void createPendingPayments_multiplePerson() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Employment employment = new Employment();
        Person newAlice = new PersonBuilder(ALICE).build();
        Person newBenson = new PersonBuilder(BENSON).build();

        employment.associate(EATING, newAlice);
        employment.associate(EATING, newBenson);

        Map<ID, Payment> alicePayments = new HashMap<>(newAlice.getPayments());
        Map<ID, Payment> bensonPayments = new HashMap<>(newBenson.getPayments());

        PaymentHandler.createPendingPayments(EATING, model, employment);

        Payment newAlicePayment = Payment.createPayment(newAlice, EATING, EATING.calculatePay(newAlice.getRate()));
        Payment newBensonPayment = Payment.createPayment(newBenson, EATING, EATING.calculatePay(newBenson.getRate()));

        alicePayments.put(EATING.getJobId(), newAlicePayment);
        bensonPayments.put(EATING.getJobId(), newBensonPayment);

        assertEquals(alicePayments, model.getPerson(newAlice.getPersonId()).getPayments());
        assertEquals(bensonPayments, model.getPerson(newBenson.getPersonId()).getPayments());
    }

    @Test
    public void removePendingPayments_throwsPaymentRequiresPersonException() {
        Employment employment = new Employment();
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        assertThrows(PaymentRequiresPersonException.class, () ->
                PaymentHandler.removePendingPayments(EATING, model, employment));
    }

    @Test
    public void removePendingPayments_singlePerson() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Employment employment = new Employment();
        Person newAlice = new PersonBuilder(ALICE).build();

        employment.associate(EATING, newAlice);
        employment.associate(RUNNING, newAlice);

        Map<ID, Payment> alicePayments = new HashMap<>(newAlice.getPayments());

        PaymentHandler.createPendingPayments(EATING, model, employment);
        PaymentHandler.createPendingPayments(RUNNING, model, employment);
        PaymentHandler.removePendingPayments(EATING, model, employment);

        Payment newAlicePayment = Payment.createPayment(newAlice, RUNNING, RUNNING.calculatePay(newAlice.getRate()));

        alicePayments.put(RUNNING.getJobId(), newAlicePayment);

        assertEquals(alicePayments, model.getPerson(newAlice.getPersonId()).getPayments());
    }

    @Test
    public void removePendingPayments_multiplePerson() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Employment employment = new Employment();

        Person newAlice = new PersonBuilder(ALICE).build();
        Person newBenson = new PersonBuilder(BENSON).build();
        Person newCarl = new PersonBuilder(CARL).build();

        employment.associate(EATING, newAlice);
        employment.associate(RUNNING, newAlice);
        employment.associate(EATING, newBenson);
        employment.associate(RUNNING, newCarl);

        Map<ID, Payment> alicePayments = new HashMap<>(newAlice.getPayments());
        Map<ID, Payment> bensonPayments = new HashMap<>(newBenson.getPayments());
        Map<ID, Payment> carlPayments = new HashMap<>(newCarl.getPayments());

        PaymentHandler.createPendingPayments(EATING, model, employment);
        PaymentHandler.createPendingPayments(RUNNING, model, employment);

        PaymentHandler.removePendingPayments(EATING, model, employment);

        Payment newAlicePayment = Payment.createPayment(newAlice, RUNNING, RUNNING.calculatePay(newAlice.getRate()));
        Payment newCarlPayment = Payment.createPayment(newCarl, RUNNING, RUNNING.calculatePay(newCarl.getRate()));

        alicePayments.put(RUNNING.getJobId(), newAlicePayment);
        carlPayments.put(RUNNING.getJobId(), newCarlPayment);

        assertEquals(alicePayments, model.getPerson(newAlice.getPersonId()).getPayments());
        assertEquals(bensonPayments, model.getPerson(newBenson.getPersonId()).getPayments());
        assertEquals(carlPayments, model.getPerson(newCarl.getPersonId()).getPayments());
    }

    @Test
    public void finalizePayments_singlePerson() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Employment employment = new Employment();
        Person newAlice = new PersonBuilder(ALICE).build();

        employment.associate(EATING, newAlice);
        employment.associate(RUNNING, newAlice);

        Map<ID, Payment> alicePayments = new HashMap<>(newAlice.getPayments());

        PaymentHandler.createPendingPayments(EATING, model, employment);
        PaymentHandler.createPendingPayments(RUNNING, model, employment);

        Payment newAliceRunningPayment = Payment.createPayment(
                newAlice, RUNNING, RUNNING.calculatePay(newAlice.getRate()));
        Payment newAliceEatingPayment = Payment.createPayment(
                newAlice, EATING, EATING.calculatePay(newAlice.getRate()));
        newAliceRunningPayment.pay();

        alicePayments.put(EATING.getJobId(), newAliceEatingPayment);
        alicePayments.put(RUNNING.getJobId(), newAliceRunningPayment);

        assertEquals(alicePayments, model.getPerson(newAlice.getPersonId()).getPayments());
    }

    @Test
    public void finalizePayments_multiplePerson() {

        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Employment employment = new Employment();

        Person newAlice = new PersonBuilder(ALICE).build();
        Person newBenson = new PersonBuilder(BENSON).build();
        Person newCarl = new PersonBuilder(CARL).build();

        employment.associate(EATING, newAlice);
        employment.associate(RUNNING, newAlice);
        employment.associate(EATING, newBenson);
        employment.associate(RUNNING, newCarl);

        Map<ID, Payment> alicePayments = new HashMap<>(newAlice.getPayments());
        Map<ID, Payment> bensonPayments = new HashMap<>(newBenson.getPayments());
        Map<ID, Payment> carlPayments = new HashMap<>(newCarl.getPayments());

        PaymentHandler.createPendingPayments(EATING, model, employment);
        PaymentHandler.createPendingPayments(RUNNING, model, employment);

        Payment newAliceRunningPayment = Payment.createPayment(
                newAlice, RUNNING, RUNNING.calculatePay(newAlice.getRate()));
        Payment newAliceEatingPayment = Payment.createPayment(
                newAlice, EATING, EATING.calculatePay(newAlice.getRate()));
        newAliceRunningPayment.pay();

        Payment newBensonEatingPayment = Payment.createPayment(
                newBenson, EATING, EATING.calculatePay(newBenson.getRate()));

        Payment newCarlRunningPayment = Payment.createPayment(
                newCarl, RUNNING, RUNNING.calculatePay(newCarl.getRate()));
        newCarlRunningPayment.pay();

        alicePayments.put(EATING.getJobId(), newAliceEatingPayment);
        alicePayments.put(RUNNING.getJobId(), newAliceRunningPayment);
        bensonPayments.put(EATING.getJobId(), newBensonEatingPayment);
        carlPayments.put(RUNNING.getJobId(), newCarlRunningPayment);

        assertEquals(alicePayments, model.getPerson(newAlice.getPersonId()).getPayments());
        assertEquals(bensonPayments, model.getPerson(newBenson.getPersonId()).getPayments());
        assertEquals(carlPayments, model.getPerson(newCarl.getPersonId()).getPayments());
    }

}
