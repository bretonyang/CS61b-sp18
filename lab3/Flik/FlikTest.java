import org.junit.Test;
import static org.junit.Assert.*;

public class FlikTest {

    @Test
    public void isSameNumberTest() {
        int a = 128, b = 128, c = 2;
        assertTrue(Flik.isSameNumber(a, b));
        assertTrue(Flik.isSameNumber(1, 1));
        assertFalse(Flik.isSameNumber(a, c));
    }

}
