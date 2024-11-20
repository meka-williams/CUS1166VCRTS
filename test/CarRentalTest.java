import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class CarRentalsTest {

    private CarRentals carRentals;

    @BeforeEach
    void setUp() {
        carRentals = new CarRentals(
            "me",
            "Camry",
            "Toyota",
            "ABC123",
            "123456789",
            "1C6RR6LT4FS541871",
            "1 day"
        );
    }

    @Test
    void testConstructorInitialization() {
        assertEquals("me", carRentals.getOwnerId(), "Owner ID should be initialized correctly.");
        assertEquals("Camry", carRentals.getVehicleModel(), "Model should be initialized correctly.");
        assertEquals("Toyota", carRentals.getVehicleBrand(), "Brand should be initialized correctly.");
        assertEquals("ABC123", carRentals.getPlateNumber(), "Plate number should be initialized correctly.");
        assertEquals("123456789", carRentals.getSerialNumber(), "Serial number should be initialized correctly.");
        assertEquals("1C6RR6LT4FS541871", carRentals.getVinNumber(), "VIN number should be initialized correctly.");
        assertEquals("1 day", carRentals.getResidencyTime(), "Residency time should be initialized correctly.");
    }

    @Test
    void testAssignJob() {
        JobRequest job1 = new JobRequest("Job1", "EX1");
        JobRequest job2 = new JobRequest("Job2", "EX2");

        carRentals.assignJob(job1);
        carRentals.assignJob(job2);
        List<JobRequest> assignedJobs = carRentals.getAssignedJobs();

        assertEquals(2, assignedJobs.size(), "Two jobs should be assigned.");
        assertTrue(assignedJobs.contains(job1), "EX1 should be here");
        assertTrue(assignedJobs.contains(job2), "EX2 should be here");
    }

    @Test
    void testGetAssignedJobs_EmptyList() {
        List<JobRequest> assignedJobs = carRentals.getAssignedJobs();

        assertTrue(assignedJobs.isEmpty(), "");
    }

    @Test
    void testAssignJob_NullJob() {
        // Act
        carRentals.assignJob(null);
        List<JobRequest> assignedJobs = carRentals.getAssignedJobs();

        // Assert
        assertEquals(0, assignedJobs.size(), "Null job should not be added to the assigned jobs list.");
    }
}