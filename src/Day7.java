import lib.InputUtil;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day7 {
    public static void main(String[] args) throws IOException {
        List<String> input = InputUtil.readAsLines("input7.txt");
        first(input);
        second(input);
    }

    private static void first(List<String> input) {
        State state = new State(createMap(input));
        System.out.println(state.run());
    }

    private static void second(List<String> input) {
        State state = new State(createMap(input));
        System.out.println(state.run(5));
    }

    private static Map<Character, Set<Character>> createMap(List<String> input) {
        Map<Character, Set<Character>> map = new HashMap<>();
        for (String s : input) {
            char c = s.charAt(36);
            char d = s.charAt(5);
            map.computeIfAbsent(c, key -> new HashSet<>()).add(d);
            map.computeIfAbsent(d, key -> new HashSet<>());
        }
        return map;
    }

    static class State {
        final Map<Character, Set<Character>> map;
        final SortedSet<Character> open = new TreeSet<>();
        final StringBuilder sb = new StringBuilder();

        public State(Map<Character, Set<Character>> map) {
            this.map = map;
        }

        public String run() {
            while (true) {
                updateOpen();
                if (open.isEmpty()) {
                    break;
                }
                char c = open.first();
                open.remove(c);
                finish(c);
            }
            return sb.toString();
        }

        public int run(int workerCount) {
            int time = 0;
            Queue<Event> queue = new PriorityQueue<>();
            while (true) {
                updateOpen();
                while (!open.isEmpty() && queue.size() < workerCount) {
                    char c = open.first();
                    open.remove(c);
                    queue.add(new Event(c, time + (c - 'A' + 61)));
                }
                if (queue.isEmpty()) {
                    break;
                }
                Event event = queue.remove();
                time = event.time;
                finish(event.c);
            }
            return time;
        }

        private void updateOpen() {
            Set<Character> justOpened = map.entrySet().stream()
                    .filter(e -> e.getValue().isEmpty())
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toSet());
            justOpened.forEach(map::remove);
            open.addAll(justOpened);
        }

        private void finish(char c) {
            sb.append(c);
            map.values().forEach(set -> set.remove(c));
        }

        static class Event implements Comparable<Event> {
            private final Comparator<Event> COMPARATOR = Comparator.comparing((Event e) -> e.time).thenComparing(e -> e.c);

            final char c;
            final int time;

            public Event(char c, int time) {
                this.c = c;
                this.time = time;
            }

            @Override
            public int compareTo(Event o) {
                return COMPARATOR.compare(this, o);
            }
        }
    }
}
