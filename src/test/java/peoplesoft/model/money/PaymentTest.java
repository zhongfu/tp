package peoplesoft.model.money;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static peoplesoft.testutil.Assert.assertThrows;
import static peoplesoft.testutil.TypicalPersons.*;

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
    private static final Rate RATE_ONE = new Rate(new Money(1), Duration.ofHours(1));

    @Test
    public void empty_personList_throwsPaymentRequiresPersonException() {
        Employment employment = new Employment();
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        assertThrows(PaymentRequiresPersonException.class,() -> PaymentHandler.createPendingPayments(EATING, model, employment));
    }

    @Test
    public void createPendingPayments() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Employment employment = new Employment();
        Person newALICE = new PersonBuilder(ALICE).build();

        employment.associate(EATING, newALICE);

        Map<ID, Payment> alicePayments = new HashMap<>(newALICE.getPayments());

        PaymentHandler.createPendingPayments(EATING, model, employment);

        Payment newAlicePayment = Payment.createPayment(newALICE, EATING, EATING.calculatePay(newALICE.getRate()));

        alicePayments.put(EATING.getJobId(), newAlicePayment);

        assertEquals(alicePayments, model.getPerson(newALICE.getPersonId()).getPayments());
    }

    @Test
    public void removePendingPayments() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Employment employment = new Employment();
        Person newALICE = new PersonBuilder(ALICE).build();

        employment.associate(EATING, newALICE);
        employment.associate(RUNNING, newALICE);

        Map<ID, Payment> alicePayments = new HashMap<>(newALICE.getPayments());

        PaymentHandler.createPendingPayments(EATING, model, employment);
        PaymentHandler.createPendingPayments(RUNNING, model, employment);
        PaymentHandler.removePendingPayments(EATING, model, employment);

        Payment newAlicePayment = Payment.createPayment(newALICE, RUNNING, RUNNING.calculatePay(newALICE.getRate()));

        alicePayments.put(RUNNING.getJobId(), newAlicePayment);

        assertEquals(alicePayments, model.getPerson(newALICE.getPersonId()).getPayments());
    }

    @Test
    public void finalizePayments() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Employment employment = new Employment();
        Person newALICE = new PersonBuilder(ALICE).build();

        employment.associate(EATING, newALICE);
        employment.associate(RUNNING, newALICE);

        Map<ID, Payment> alicePayments = new HashMap<>(newALICE.getPayments());

        PaymentHandler.createPendingPayments(EATING, model, employment);
        PaymentHandler.createPendingPayments(RUNNING, model, employment);

        Payment newAliceRunningPayment = Payment.createPayment(newALICE, RUNNING, RUNNING.calculatePay(newALICE.getRate()));
        Payment newAliceEatingPayment = Payment.createPayment(newALICE, EATING, EATING.calculatePay(newALICE.getRate()));
        newAliceRunningPayment.pay();

        alicePayments.put(EATING.getJobId(), newAliceEatingPayment);
        alicePayments.put(RUNNING.getJobId(), newAliceRunningPayment);

        assertEquals(alicePayments, model.getPerson(newALICE.getPersonId()).getPayments());
    }

}
