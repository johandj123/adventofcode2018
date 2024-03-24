package lib;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CollectionUtil {
    private CollectionUtil() {
    }

    public static <T> Map<T, Integer> calculateHistogram(Collection<T> input) {
        Map<T, Integer> histogram = new HashMap<>();
        for (T value : input) {
            histogram.put(value, histogram.getOrDefault(value, 0) + 1);
        }
        return histogram;
    }

    public static <T> T leastCommonValue(Collection<T> input) {
        Map<T, Integer> histogram = calculateHistogram(input);
        return histogram.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow();
    }

    public static <T> T mostCommonValue(Collection<T> input) {
        Map<T, Integer> histogram = calculateHistogram(input);
        return histogram.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow();
    }
}
