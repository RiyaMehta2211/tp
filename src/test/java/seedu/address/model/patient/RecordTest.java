package seedu.address.model.patient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PatientBuilder;
import seedu.address.testutil.RecordBuilder;

public class RecordTest {

    // Example valid patient for testing
    private final Patient validPatient = new PatientBuilder().build();

    @Test
    public void isValidRecord() {
        // This method should return true if the record is valid and false otherwise.

        // Test valid record
        Record validRecord = new Record(validPatient);
        // Example valid and invalid strings for testing
        String validObservation = "This is a valid observation";
        validRecord.setInitialObservations(validObservation);
        String validDiagnosis = "Diagnosis Example";
        validRecord.setDiagnosis(validDiagnosis);
        String validTreatmentPlan = "Treatment Example Plan";
        validRecord.setTreatmentPlan(validTreatmentPlan);
        assertTrue(isValidRecord(validRecord));

        // Test invalid record
        Record invalidRecord = new Record(validPatient);
        // assuming empty string is invalid
        String invalidObservation = "";
        invalidRecord.setInitialObservations(invalidObservation);
        invalidRecord.setDiagnosis(validDiagnosis);
        invalidRecord.setTreatmentPlan(validTreatmentPlan);
        assertFalse(isValidRecord(invalidRecord));
    }

    private boolean isValidRecord(Record record) {
        return record.getPatient() != null
                && !record.getInitialObservations().isEmpty()
                && !record.getDiagnosis().isEmpty()
                && !record.getTreatmentPlan().isEmpty();
    }

    @Test
    public void getInitialObservations() {
        String initialObservations = "Patient shows signs of fatigue.";
        Record record = new RecordBuilder().withInitialObservations(initialObservations).build();
        assertEquals(initialObservations, record.getInitialObservations());
    }

    @Test
    public void setInitialObservations() {
        Record record = new RecordBuilder().build();
        String newInitialObservations = "Patient has a high fever.";
        record.setInitialObservations(newInitialObservations);
        assertEquals(newInitialObservations, record.getInitialObservations());
    }

    @Test
    public void getDiagnosis() {
        String diagnosis = "Migraine";
        Record record = new RecordBuilder().withDiagnosis(diagnosis).build();
        assertEquals(diagnosis, record.getDiagnosis());
    }

    @Test
    public void setDiagnosis() {
        Record record = new RecordBuilder().build();
        String newDiagnosis = "Flu";
        record.setDiagnosis(newDiagnosis);
        assertEquals(newDiagnosis, record.getDiagnosis());
    }

    @Test
    public void getTreatmentPlan() {
        String treatmentPlan = "Paracetamol 500mg twice a day for a week.";
        Record record = new RecordBuilder().withTreatmentPlan(treatmentPlan).build();
        assertEquals(treatmentPlan, record.getTreatmentPlan());
    }

    @Test
    public void setTreatmentPlan() {
        Record record = new RecordBuilder().build();
        String newTreatmentPlan = "Rest and drink plenty of fluids.";
        record.setTreatmentPlan(newTreatmentPlan);
        assertEquals(newTreatmentPlan, record.getTreatmentPlan());
    }

    @Test
    public void equals() {
        Record record1 = new RecordBuilder().build();
        Record record2 = new RecordBuilder().withInitialObservations("Different observation").build();

        // same object -> returns true
        assertTrue(record1.equals(record1));

        // same values -> returns false
        Record record1Copy = new RecordBuilder().build();
        assertFalse(record1.equals(record1Copy));

        // null -> returns false
        assertFalse(record1.equals(null));

        // different type -> returns false
        assertFalse(record1.equals(5));

        // different record -> returns false
        assertFalse(record1.equals(record2));
    }

    @Test
    public void testToString() {
        Record record = new RecordBuilder().build();
        String expected = Record.class.getCanonicalName() + "{patient=" + record.getPatient()
                + ", initialObservations=" + record.getInitialObservations()
                + ", diagnosis=" + record.getDiagnosis()
                + ", treatmentPlan=" + record.getTreatmentPlan() + "}";
        assertEquals(expected, record.toString());
    }
}
