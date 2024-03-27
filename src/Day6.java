import lib.Counter;
import lib.InputUtil;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day6 {
    public static void main(String[] args) throws IOException {
        List<Position> input = InputUtil.readAsLines("input6.txt").stream()
                .map(Position::new)
                .collect(Collectors.toList());
        int x0 = input.stream().mapToInt(p -> p.x).min().orElseThrow() - 1;
        int x1 = input.stream().mapToInt(p -> p.x).max().orElseThrow() + 1;
        int y0 = input.stream().mapToInt(p -> p.y).min().orElseThrow() - 1;
        int y1 = input.stream().mapToInt(p -> p.y).max().orElseThrow() + 1;
        first(y0, y1, x0, x1, input);
        second(y0, y1, x0, x1, input);
    }

    private static void first(int y0, int y1, int x0, int x1, List<Position> input) {
        Set<Integer> infinite = new HashSet<>();
        Counter<Integer> counter = new Counter<>();
        for (int y = y0; y <= y1; y++) {
            for (int x = x0; x <= x1; x++) {
                Position position = new Position(x, y);
                Integer index = closest(input, position);
                if (index != null) {
                    if (x == x0 || x == x1 || y == y0 || y == y1) {
                        infinite.add(index);
                    } else {
                        counter.inc(index);
                    }
                }
            }
        }
        int maxCount = counter.entrySet().stream()
                .filter(e -> !infinite.contains(e.getKey()))
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getValue)
                .orElseThrow();
        System.out.println(maxCount);
    }

    private static void second(int y0, int y1, int x0, int x1, List<Position> input) {
        int count = 0;
        for (int y = y0; y <= y1; y++) {
            for (int x = x0; x <= x1; x++) {
                if (totalDistance(input, new Position(x, y)) < 10000) {
                    count++;
                }
            }
        }
        System.out.println(count);
    }

    static Integer closest(List<Position> input, Position position) {
        int minDistance = input.stream().mapToInt(p -> p.distance(position)).min().orElseThrow();
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            if (position.distance(input.get(i)) == minDistance) {
                indexes.add(i);
            }
        }
        return indexes.size() == 1 ? indexes.get(0) : null;
    }

    static int totalDistance(List<Position> input, Position position) {
        return input.stream().mapToInt(p -> p.distance(position)).sum();
    }

    static class Position {
        final int x;
        final int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Position(String s) {
            String[] sp = s.split(", ");
            x = Integer.parseInt(sp[0]);
            y = Integer.parseInt(sp[1]);
        }

        public int distance(Position o) {
            return Math.abs(x - o.x) + Math.abs(y - o.y);
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
    }
}
