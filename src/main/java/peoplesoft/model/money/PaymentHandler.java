package peoplesoft.model.money;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import peoplesoft.model.Model;
import peoplesoft.model.employment.Employment;
import peoplesoft.model.job.Job;
import peoplesoft.model.money.exceptions.PaymentRequiresPersonException;
import peoplesoft.model.person.Person;
import peoplesoft.model.util.ID;

/**
 * Class that handles all the coupling for the creation of {@code Payment} objects.
 */
public class PaymentHandler {
    // TODO: reduce time complexity if anyone wants to flex their CS2040S

    /**
     * Creates {@code PendingPayment} objects for each {@code Person} assigned
     * to a {@code Job}.
     *
     * @param job Job.
     * @param model Model.
     * @param emp Employment instance. (Mainly for testing)
     * @throws PaymentRequiresPersonException If there is no {@code Person} assigned to the {@code Job}.
     */
    public static void createPendingPayments(Job job, Model model, Employment emp) {
        assert !job.isFinal();
        List<Person> persons = emp.getPersons(job, model);
        if (persons.isEmpty()) {
            throw new PaymentRequiresPersonException();
        }
        for (Person p : persons) {
            Map<ID, Payment> payments = new HashMap<>(p.getPayments());
            Payment newPayment = Payment.createPayment(p, job, job.calculatePay(p.getRate()));
            payments.put(job.getJobId(), newPayment);
            Person newPerson = new Person(p.getPersonId(), p.getName(), p.getPhone(), p.getEmail(),
                    p.getAddress(), p.getRate(), p.getTags(), payments);
            model.setPerson(p, newPerson); // I hope this does not cause iteration errors
        }
    }

    /**
     * Removes {@code PendingPayment} objects for each {@code Person} assigned
     * to a {@code Job}.
     *
     * @param job Job.
     * @param model Model.
     * @param emp Employment instance. (Mainly for testing)
     * @throws PaymentRequiresPersonException If there is no {@code Person} assigned to the {@code Job}.
     */
    public static void removePendingPayments(Job job, Model model, Employment emp) {
        assert !job.isFinal();
        // Currently checks all persons, in case there is a user who assigns, marks, un-assigns by editing
        // the data file, and un-marks.
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        List<Person> persons = model.getFilteredPersonList();
        if (persons.isEmpty()) {
            throw new PaymentRequiresPersonException();
        }
        for (Person p : persons) {
            Map<ID, Payment> payments = new HashMap<>(p.getPayments());
            payments.entrySet().removeIf(e -> e.getKey().equals(job.getJobId()) && !e.getValue().isCompleted());
            Person newPerson = new Person(p.getPersonId(), p.getName(), p.getPhone(), p.getEmail(),
                    p.getAddress(), p.getRate(), p.getTags(), payments);
            model.setPerson(p, newPerson); // I hope this does not cause iteration errors
        }
    }

    /**
     * Converts {@code PendingPayment} objects into {@code CompletedPayment} objects.
     *
     * @param job Job.
     * @param model Model.
     * @param emp Employment instance. (Mainly for testing)
     * @throws PaymentRequiresPersonException If there is no {@code Person} assigned to the {@code Job}.
     */
    public static void finalizePayments(Job job, Model model, Employment emp) {
        assert job.isFinal();
        List<Person> persons = emp.getPersons(job, model);
        if (persons.isEmpty()) {
            throw new PaymentRequiresPersonException();
        }
        for (Person p : persons) {
            Map<ID, Payment> payments = new HashMap<>(p.getPayments());
            payments.replaceAll((id, payment) -> id.equals(job.getJobId()) ? payment.pay() : payment);
            Person newPerson = new Person(p.getPersonId(), p.getName(), p.getPhone(), p.getEmail(),
                    p.getAddress(), p.getRate(), p.getTags(), payments);
            model.setPerson(p, newPerson); // I hope this does not cause iteration errors
        }
    }
}
