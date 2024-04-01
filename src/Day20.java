import lib.GraphUtil;
import lib.InputUtil;

import java.io.IOException;
import java.util.*;

public class Day20 {
    private static final List<Position> DIRECTION = List.of(new Position(-1, 0), new Position(1, 0), new Position(0, -1), new Position(0, 1));

    private final Map<Position, Character> field = new HashMap<>();

    public static void main(String[] args) throws IOException {
        new Day20().start();
    }

    private void start() throws IOException {
        String input = InputUtil.readAsString("input20.txt").replace("^", "").replace("$", "");
        Position start = new Position(0, 0);
        field.put(start, '.');
        processSerial(input, Set.of(start));
        print();
        first(start);
        second(start);
    }

    private void first(Position start) {
        int distance = GraphUtil.breadthFirstSearch(start, this::getNeighbours);
        System.out.println(distance / 2);
    }

    private void second(Position start) {
        Set<Position> set = GraphUtil.reachable(start, this::getNeighbours);
        set.removeAll(GraphUtil.breadthFirstSearch(start, this::getNeighbours, 999 * 2));
        long count = set.stream().filter(position -> field.get(position) == '.').count();
        System.out.println(count);
    }

    private List<Position> getNeighbours(Position position) {
        List<Position> result = new ArrayList<>();
        for (Position delta : DIRECTION) {
            Position next = position.add(delta);
            char c = field.getOrDefault(next, '#');
            if (c == '.' || c == '|' || c == '-') {
                result.add(next);
            }
        }
        return result;
    }

    private Set<Position> processSerial(String s, Set<Position> setIn) {
        Set<Position> set = new HashSet<>(setIn);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case 'W':
                    set = move(set, -1, 0);
                    break;
                case 'E':
                    set = move(set, 1, 0);
                    break;
                case 'N':
                    set = move(set, 0, -1);
                    break;
                case 'S':
                    set = move(set, 0, 1);
                    break;
                case '(':
                    int balance = 1;
                    i++;
                    int start = i;
                    while (balance > 0) {
                        c = s.charAt(i++);
                        if (c == '(') {
                            balance++;
                        } else if (c == ')') {
                            balance--;
                        }
                    }
                    i--;
                    int end = i;
                    set = processParallel(s.substring(start, end), set);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown character " + c);
            }
        }
        return set;
    }

    private Set<Position> move(Set<Position> set,int dx,int dy) {
        char c = (dx == 0) ? '-' : '|';
        Set<Position> result = new HashSet<>();
        for (Position position : set) {
            field.put(position.add(dx, dy), c);
            field.put(position.add(dx * 2, dy * 2), '.');
            result.add(position.add(dx * 2, dy * 2));
        }
        return result;
    }

    private Set<Position> processParallel(String s, Set<Position> setIn) {
        List<String> parts = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        int balance = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') {
                balance++;
            } else if (c == ')') {
                balance--;
            } else if (c == '|' && balance == 0) {
                parts.add(sb.toString());
                sb = new StringBuilder();
                continue;
            }
            sb.append(c);
        }
        parts.add(sb.toString());
        Set<Position> result = new HashSet<>();
        for (String part : parts) {
            result.addAll(processSerial(part, setIn));
        }
        return result;
    }

    private void print() {
        int xmin = field.keySet().stream().mapToInt(p -> p.x).min().orElseThrow();
        int xmax = field.keySet().stream().mapToInt(p -> p.x).max().orElseThrow();
        int ymin = field.keySet().stream().mapToInt(p -> p.y).min().orElseThrow();
        int ymax = field.keySet().stream().mapToInt(p -> p.y).max().orElseThrow();
        for (int y = ymin - 1; y <= ymax + 1; y++) {
            for (int x = xmin - 1; x <= xmax + 1; x++) {
                if (x == 0 && y == 0) {
                    System.out.print('X');
                } else {
                    System.out.print(field.getOrDefault(new Position(x, y), '#'));
                }
            }
            System.out.println();
        }
    }

    private static class Position {
        final int x;
        final int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Position add(int dx,int dy) {
            return new Position(x + dx, y + dy);
        }

        public Position add(Position o) {
            return new Position(x + o.x, y + o.y);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return x == position.x && y == position.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return String.format("%d,%d", x, y);
        }
    }
}
