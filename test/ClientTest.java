import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;


public class ClientTest 
{
    private Client client;
    private JobRequest jobRequest1;
    private JobRequest jobRequest2;

    @BeforeEach
    public void setUp() {
        client = new Client("bb1", "password");
        jobRequest1 = new JobRequest("Job 1 ");
        jobRequest2 = new JobRequest("Job 2 ");
    }

    @Test
    public void testConstructorInitialization() {
        assertEquals("bb1", client.getUsername());
        assertTrue(client.getRequestedJobs().isEmpty(), "Should be empty at first");
    }

    @Test
    public void testSubmitJob() {
        client.submitJob(jobRequest1);
        assertEquals(1, client.getRequestedJobs().size());
        assertEquals(jobRequest1, client.getRequestedJobs().get(0));

        client.submitJob(jobRequest2);
        assertEquals(2, client.getRequestedJobs().size());
    }

    @Test
    public void testGetQueuedJobs() {
        client.submitJob(jobRequest1);
        client.submitJob(jobRequest2);

        String expectedOutput = jobRequest1.toString() + jobRequest2.toString();
        assertEquals(expectedOutput, client.getQueuedJobs());
    }

    @Test
    public void testToString() {
        client.submitJob(jobRequest1);
        String expectedOutput = "Client ID: bb1" + jobRequest1.toString();
        assertEquals(expectedOutput, client.toString());
    }
}
}

