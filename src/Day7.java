import lib.InputUtil;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day7 {
    public static void main(String[] args) throws IOException {
        List<String> input = InputUtil.readAsLines("input7.txt");
        Map<Character, Set<Character>> map = new HashMap<>();
        for (String s : input) {
            char c = s.charAt(36);
            char d = s.charAt(5);
            map.computeIfAbsent(c, key -> new HashSet<>()).add(d);
            map.computeIfAbsent(d, key -> new HashSet<>());
        }
        SortedSet<Character> open = new TreeSet<>();
        StringBuilder sb = new StringBuilder();
        do {
            Set<Character> justOpened = map.entrySet().stream()
                    .filter(e -> e.getValue().isEmpty())
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toSet());
            justOpened.forEach(map::remove);
            open.addAll(justOpened);
            if (open.isEmpty()) {
                break;
            }
            char c = open.first();
            open.remove(c);
            sb.append(c);
            map.values().forEach(set -> set.remove(c));
        } while (true);
        System.out.println(sb);
    }
}
