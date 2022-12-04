import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDequeGold {

    @Test
    public void test() {
        StudentArrayDeque<Integer> sad = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> ads = new ArrayDequeSolution<>();

        int i = 0;
        String message = "\n";
        while (true) {
            int option = (int) (StdRandom.uniform() * 4);
            
            if (option == 0) {
                sad.addFirst(i);
                ads.addFirst(i);
                message += "addFirst(" + i + ")\n";
            } else if (option == 1) {
                sad.addLast(i);
                ads.addLast(i);
                message += "addLast(" + i + ")\n";
            } else if (option == 2) {
                if (!ads.isEmpty()) {
                    Integer actual = sad.removeFirst();
                    Integer expected = ads.removeFirst();
                    message += "removeFirst()\n";
                    assertEquals(message, expected, actual);
                }
            } else if (option == 3) {
                if (!ads.isEmpty()) {
                    Integer actual = sad.removeLast();
                    Integer expected = ads.removeLast();
                    message += "removeLast()\n";
                    assertEquals(message, expected, actual);
                }
            }
            i++;
        }
    }

}
