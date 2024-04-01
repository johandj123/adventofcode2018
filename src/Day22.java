import lib.InputUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Day22 {
    private int depth;
    private Position target;
    private Map<Position, Long> geologicIndexCache = new HashMap<>();

    public static void main(String[] args) throws IOException {
        new Day22().start();
    }

    private void start() throws IOException {
        List<Integer> input = InputUtil.extractPositiveIntegers(InputUtil.readAsString("input22.txt"));
        depth = input.get(0);
        target = new Position(input.get(1), input.get(2));

        int sum = 0;
        for (int y = 0; y <= target.y; y++) {
            for (int x = 0; x <= target.x; x++) {
                int type = type(new Position(x, y));
                System.out.print(".=|".charAt(type));
                sum += type;
            }
            System.out.println();
        }
        sum -= type(target);
        System.out.println(sum);
    }

    private long geologicIndex(Position position) {
        if (geologicIndexCache.containsKey(position)) {
            return geologicIndexCache.get(position);
        }
        long result;
        if (position.y == 0) {
            result = 16807L * position.x;
        } else if (position.x == 0) {
            result = 48271L * position.y;
        } else {
            result = erosionLevel(position.add(-1, 0)) * erosionLevel(position.add(0, -1));
        }
        geologicIndexCache.put(position, result);
        return result;
    }

    private long erosionLevel(Position position) {
        return (geologicIndex(position) + depth) % 20183L;
    }

    private int type(Position position) {
        long e = erosionLevel(position);
        return (int)(e % 3);
    }

    static class Position {
        final int x;
        final int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Position add(int dx,int dy) {
            return new Position(x + dx, y + dy);
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
