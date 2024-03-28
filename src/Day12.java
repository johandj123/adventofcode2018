import lib.InputUtil;

import java.io.IOException;
import java.util.*;

public class Day12 {
    public static void main(String[] args) throws IOException {
        List<String> input = InputUtil.readAsStringGroups("input12.txt");
        String initialString = input.get(0).substring(15);
        Set<Integer> initialState = new HashSet<>();
        for (int i = 0; i < initialString.length(); i++) {
            if (initialString.charAt(i) == '#') {
                initialState.add(i);
            }
        }
        Map<String, Boolean> rules = new HashMap<>();
        for (String s : input.get(1).split("\n")) {
            String[] sp = s.split(" => ");
            rules.put(sp[0], sp[1].charAt(0) == '#');
        }
        Set<Integer> state = new HashSet<>(initialState);
        for (int i = 0; i < 20; i++) {
            state = evolve(state, rules);
        }
        long sum = state.stream().mapToInt(x -> x).sum();
        System.out.println(sum);
    }

    private static String stateAsString(Set<Integer> state,int position) {
        StringBuilder sb = new StringBuilder();
        for (int i = position - 2; i <= position + 2; i++) {
            sb.append(state.contains(i) ? '#' : '.');
        }
        return sb.toString();
    }

    private static Set<Integer> evolve(Set<Integer> state, Map<String, Boolean> rules) {
        int min = state.stream().mapToInt(x -> x).min().orElseThrow();
        int max = state.stream().mapToInt(x -> x).max().orElseThrow();
        Set<Integer> nextState = new HashSet<>();
        for (int i = min - 4; i <= max + 4; i++) {
            if (rules.getOrDefault(stateAsString(state, i), false)) {
                nextState.add(i);
            }
        }
        return nextState;
    }
}
