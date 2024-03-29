package lib;

import java.util.ArrayList;
import java.util.List;

public class PredictUtil {
    private PredictUtil() {
    }

    public static Predictor predict(List<Long> values) {
        List<Long> delta1 = calculateDeltas(values);
        List<Long> delta2 = calculateDeltas(delta1);
        if (sameNumbers(delta2)) {
            return new Predictor(values.get(0), delta1.get(0), delta2.get(0));
        }
        return null;
    }

    private static List<Long> calculateDeltas(List<Long> values) {
        List<Long> result = new ArrayList<>();
        for (int i = 0; i < values.size() - 1; i++) {
            result.add(values.get(i + 1) - values.get(i));
        }
        return result;
    }

    private static boolean sameNumbers(List<Long> values) {
        if (values.size() < 2) {
            return false;
        }
        long first = values.get(0);
        return values.stream().allMatch(value -> value == first);
    }

    public static class Predictor {
        final long x0;
        final long x1;
        final long x2;

        public Predictor(long x0, long x1, long x2) {
            this.x0 = x0;
            this.x1 = x1;
            this.x2 = x2;
        }

        public long get(long index) {
            return x0 + index * x1 + x2 * (((index - 1) * (index - 1) + index) / 2);
        }
    }
}
