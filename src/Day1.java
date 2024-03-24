import lib.InputUtil;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day1 {
    public static void main(String[] args) throws IOException {
        List<Integer> input = InputUtil.readAsLines("input1.txt").stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        first(input);
        second(input);
    }

    private static void first(List<Integer> input) {
        System.out.println(input.stream().mapToInt(x -> x).sum());
    }

    private static void second(List<Integer> input) {
        int current = 0;
        Set<Integer> seen = new HashSet<>(List.of(current));
        while (true) {
            for (int i : input) {
                current += i;
                if (!seen.add(current)) {
                    System.out.println(current);
                    return;
                }
            }
        }
    }
}
