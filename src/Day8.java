import lib.InputUtil;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Day8 {
    public static void main(String[] args) throws IOException {
        List<Integer> input = InputUtil.extractPositiveIntegers(InputUtil.readAsString("input8.txt"));
        first(input);
        second(input);
    }

    private static void first(List<Integer> input) {
        Deque<Integer> deque = new ArrayDeque<>(input);
        int metdata = sumMetadata(deque);
        if (!deque.isEmpty()) {
            throw new IllegalStateException("Input not completely consumed");
        }
        System.out.println(metdata);
    }

    private static void second(List<Integer> input) {
        Deque<Integer> deque = new ArrayDeque<>(input);
        int value = determineValue(deque);
        if (!deque.isEmpty()) {
            throw new IllegalStateException("Input not completely consumed");
        }
        System.out.println(value);
    }

    private static int sumMetadata(Deque<Integer> deque) {
        int childCount = deque.removeFirst();
        int metaCount = deque.removeFirst();
        int result = 0;
        for (int i = 0; i < childCount; i++) {
            result += sumMetadata(deque);
        }
        for (int i = 0; i < metaCount; i++) {
            result += deque.removeFirst();
        }
        return result;
    }

    private static int determineValue(Deque<Integer> deque) {
        int childCount = deque.removeFirst();
        int metaCount = deque.removeFirst();
        List<Integer> childValues = new ArrayList<>();
        for (int i = 0; i < childCount; i++) {
            childValues.add(determineValue(deque));
        }
        List<Integer> metaValues = new ArrayList<>();
        for (int i = 0; i < metaCount; i++) {
            metaValues.add(deque.removeFirst());
        }
        if (childCount == 0) {
            return metaValues.stream().mapToInt(x -> x).sum();
        } else {
            int result = 0;
            for (int meta : metaValues) {
                if (1 <= meta && meta <= childValues.size()) {
                    result += childValues.get(meta - 1);
                }
            }
            return result;
        }
    }
}
