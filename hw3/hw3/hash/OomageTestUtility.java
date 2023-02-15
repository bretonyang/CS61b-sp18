package hw3.hash;

import java.util.List;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        /*
         * Write a utility function that returns true if the given oomages
         * have hashCodes that would distribute them fairly evenly across
         * M buckets. To do this, convert each oomage's hashcode in the
         * same way as in the visualizer, i.e. (& 0x7FFFFFFF) % M.
         * and ensure that no bucket has fewer than N / 50
         * Oomages and no bucket has more than N / 2.5 Oomages.
         */
        int[] buckets = new int[M];

        // Record number of oomages in each bucket
        for (Oomage oomage : oomages) {
            // integer & 0x7fffffff sets the sign bit of integer to 0
            int bucketNum = (oomage.hashCode() & 0x7fffffff) % M;
            buckets[bucketNum]++;
        }

        // Check bucket sizes are uniformly spread.
        for (int i = 0; i < M; i++) {
            int N = oomages.size();
            // i.e. size <= N/50 or size >= N/2.5
            if ((50 * buckets[i] <= N) || (5 * buckets[i] >= 2 * N)) {
                return false;
            }
        }
        return true;
    }
}
