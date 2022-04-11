package peoplesoft.model.money;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static peoplesoft.testutil.Assert.assertThrows;
import static peoplesoft.testutil.TypicalPersons.ALICE;
import static peoplesoft.testutil.TypicalPersons.getTypicalAddressBook;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

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
    public void empty_personList_throwsPaymentRequiresPersonException() {
        Employment employment = new Employment();
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        assertThrows(PaymentRequiresPersonException.class, () ->
                PaymentHandler.createPendingPayments(EATING, model, employment));
    }

    @Test
    public void createPendingPayments() {
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
    public void removePendingPayments() {
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
    public void finalizePayments() {
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

}
