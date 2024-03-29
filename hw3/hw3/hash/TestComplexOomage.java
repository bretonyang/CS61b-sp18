package hw3.hash;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

public class TestComplexOomage {

    @Test
    public void testHashCodeDeterministic() {
        ComplexOomage so = ComplexOomage.randomComplexOomage();
        int hashCode = so.hashCode();
        for (int i = 0; i < 100; i += 1) {
            assertEquals(hashCode, so.hashCode());
        }
    }

    /* This should pass if your OomageTestUtility.haveNiceHashCodeSpread
       is correct. This is true even though our given ComplexOomage class
       has a flawed hashCode. */
    @Test
    public void testRandomOomagesHashCodeSpread() {
        List<Oomage> oomages = new ArrayList<>();
        int N = 10000;

        for (int i = 0; i < N; i += 1) {
            oomages.add(ComplexOomage.randomComplexOomage());
        }

        assertTrue(OomageTestUtility.haveNiceHashCodeSpread(oomages, 10));
    }

    /* Create a list of Complex Oomages called deadlyList
     * that shows the flaw in the hashCode function.
     */
    @Test
    public void testWithDeadlyParams() {
        List<Oomage> deadlyList = new ArrayList<>();

        // My code
        int N = 500;
        int numItemsOfList = 4;
        for (int i = 0; i < N; i++) {
            List<Integer> list = new ArrayList<>(numItemsOfList);

            // fill in until last 4 items
            int j = 0;
            for (; j < numItemsOfList - 4; j++) {
                list.add(StdRandom.uniform(0, 256));
            }
            // fill in last 4 items with the same value.
            for (; j < numItemsOfList; j++) {
                list.add(69);
            }

            ComplexOomage oomage = new ComplexOomage(list);
            deadlyList.add(oomage);
            numItemsOfList++;
            // NOTE: As long as the last 4 items in the params list of oomage is the same
            // oomage.hashCode() is the same (see notes in ComplexOomage.hashCode)
//            System.out.println(oomage.hashCode());
        }

//        HashTableVisualizer.visualize(deadlyList, 255, 0.5);
        assertTrue(OomageTestUtility.haveNiceHashCodeSpread(deadlyList, 10));
    }

    /**
     * Calls tests for SimpleOomage.
     */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestComplexOomage.class);
    }
}
