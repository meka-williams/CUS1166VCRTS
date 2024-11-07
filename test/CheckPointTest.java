import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;


public class CheckpointTest {


    private Checkpoint checkpoint;


    @Before
    public void setUp() {
        checkpoint = new Checkpoint(1, 101, 201);
    }


    @Test
    public void testCreateImage() {
        assertNotNull("createImage should not beull", checkpoint.createImage());
    }


    @Test
    public void testCopyToNewVehicle() {
        Vehicle vehicle = new Vehicle();
        checkpoint.copyToNewVehicle(vehicle);
        assertEquals(checkpoint.getCheckpointID(), vehicle.getLastCheckpointID());
    }


    @Test
    public void testRestartJob() {
        checkpoint.restartJob();
        assertEquals("Should be 100.0", 100.0f, checkpoint.getPercentLeft(), 0.0f);
    }


    @Test
    public void testGetAndSet() {
        checkpoint.setCheckpointID(2);
        assertEquals(2, checkpoint.getCheckpointID());


        checkpoint.setJobID(102);
        assertEquals(102, checkpoint.getJobID());


        checkpoint.setVehicleID(202);
        assertEquals(202, checkpoint.getVehicleID());


        checkpoint.setPercentLeft(50.0f);
        assertEquals(50.0f, checkpoint.getPercentLeft(), 0.0f);


        Object img = new Object();
        checkpoint.setImage(img);
        assertEquals(img, checkpoint.getImage());
    }
}
