import lib.InputUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Day3 {
    public static void main(String[] args) throws IOException {
        List<String> input = InputUtil.readAsLines("input3.txt");
        first(input);
        second(input);
    }

    private static void first(List<String> input) {
        Map<Position, Integer> map = new HashMap<>();
        for (String s : input) {
            List<Integer> l = InputUtil.extractPositiveIntegers(s);
            int x0 = l.get(1);
            int y0 = l.get(2);
            int w = l.get(3);
            int h = l.get(4);
            for (int y = y0; y < y0 + h; y++) {
                for (int x = x0; x < x0 + w; x++) {
                    Position position = new Position(x, y);
                    map.put(position, map.getOrDefault(position, 0) + 1);
                }
            }
        }
        int count = 0;
        for (var entry : map.entrySet()) {
            if (entry.getValue() > 1) {
                count++;
            }
        }
        System.out.println(count);
    }

    private static void second(List<String> input) {
        Map<Position, Integer> map = new HashMap<>();
        for (String s : input) {
            List<Integer> l = InputUtil.extractPositiveIntegers(s);
            int id = l.get(0);
            int x0 = l.get(1);
            int y0 = l.get(2);
            int w = l.get(3);
            int h = l.get(4);
            for (int y = y0; y < y0 + h; y++) {
                for (int x = x0; x < x0 + w; x++) {
                    Position position = new Position(x, y);
                    if (map.containsKey(position)) {
                        map.put(position, -1);
                    } else {
                        map.put(position, id);
                    }
                }
            }
        }
        for (String s : input) {
            List<Integer> l = InputUtil.extractPositiveIntegers(s);
            int id = l.get(0);
            int x0 = l.get(1);
            int y0 = l.get(2);
            int w = l.get(3);
            int h = l.get(4);
            boolean intact = true;
            outer:
            for (int y = y0; y < y0 + h; y++) {
                for (int x = x0; x < x0 + w; x++) {
                    Position position = new Position(x, y);
                    if (map.get(position) != id) {
                        intact = false;
                        break outer;
                    }
                }
            }
            if (intact) {
                System.out.println(id);
            }
        }
    }

    static class Position {
        final int x;
        final int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
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
