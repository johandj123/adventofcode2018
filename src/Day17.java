import lib.InputUtil;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Day17 {
    public static void main(String[] args) throws IOException {
        Set<Position> clay = readInput();
        Simulator simulator = new Simulator(clay);
        simulator.simulate();
        simulator.print();
        System.out.println(simulator.count());
    }

    private static Set<Position> readInput() throws IOException {
        List<String> input = InputUtil.readAsLines("input17.txt");
        Set<Position> clay = new HashSet<>();
        for (String s : input) {
            List<Integer> values = InputUtil.extractPositiveIntegers(s);
            if (s.startsWith("x")) {
                int x = values.get(0);
                int y1 = values.get(1);
                int y2 = values.get(2);
                for (int y = y1; y <= y2; y++) {
                    clay.add(new Position(x, y));
                }
            } else {
                int y = values.get(0);
                int x1 = values.get(1);
                int x2 = values.get(2);
                for (int x = x1; x <= x2; x++) {
                    clay.add(new Position(x, y));
                }
            }
        }
        return clay;
    }

    static class Simulator {
        final Set<Position> clay;
        final int ymin;
        final int ymax;
        final Set<Position> reach = new HashSet<>();
        final Set<Position> rest = new HashSet<>();

        public Simulator(Set<Position> clay) {
            this.clay = clay;
            this.ymin = clay.stream().mapToInt(p -> p.y).min().orElseThrow();
            this.ymax = clay.stream().mapToInt(p -> p.y).max().orElseThrow();
        }

        public void simulate() {
            Position springPosition = new Position(500, ymin);
            simulateDown(springPosition);
        }

        private void simulateDown(Position position) {
            int y0 = position.y;
            while (position.y < ymax && !clayOrRestBelow(position)) {
                position = position.add(0, 1);
            }
            if (position.y < ymax) {
                while (simulateSideways(position)) {
                    position = position.add(0, -1);
                }
            }
            for (int y = y0; y <= position.y; y++) {
                reach.add(new Position(position.x, y));
            }
        }

        private boolean simulateSideways(Position position) {
            if (reach.contains(position)) {
                return false;
            }
            boolean result = false;
            int x1 = position.x;
            int x2 = position.x;
            int y = position.y;
            while (!clayOrRest(new Position(x1 - 1, y))) {
                x1--;
                if (!clayOrRestBelow(new Position(x1, y))) {
                    simulateDown(new Position(x1, y + 1));
                    if (!clayOrRestBelow(new Position(x1, y))) {
                        break;
                    }
                }
            }
            while (!clayOrRest(new Position(x2 + 1, y))) {
                x2++;
                if (!clayOrRestBelow(new Position(x2, y))) {
                    simulateDown(new Position(x2, y + 1));
                    if (!clayOrRestBelow(new Position(x2, y))) {
                        break;
                    }
                }
            }
            for (int x = x1; x <= x2; x++) {
                reach.add(new Position(x, y));
            }
            if (clayOrRestBelow(new Position(x1, y)) && clayOrRestBelow(new Position(x2, y))) {
                for (int x = x1; x <= x2; x++) {
                    rest.add(new Position(x, y));
                }
                result = true;
            }
            return result;
        }

        private boolean clayOrRestBelow(Position position) {
            Position below = position.add(0, 1);
            return clayOrRest(below);
        }

        private boolean clayOrRest(Position below) {
            return clay.contains(below) || rest.contains(below);
        }

        public void print() {
            int xmin = clay.stream().mapToInt(p -> p.x).min().orElseThrow() - 1;
            int xmax = clay.stream().mapToInt(p -> p.x).max().orElseThrow() + 1;
            for (int y = 0; y <= ymax; y++) {
                for (int x = xmin; x <= xmax; x++) {
                    Position position = new Position(x, y);
                    if (clay.contains(position)) {
                        System.out.print('#');
                    } else if (rest.contains(position)) {
                        System.out.print('~');
                    } else if (reach.contains(position)) {
                        System.out.print('|');
                    } else {
                        System.out.print('.');
                    }
                }
                System.out.println();
            }
            System.out.println();
        }

        public int count() {
            return reach.size();
        }
    }

    static class Position {
        final int x;
        final int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Position add(int dx, int dy) {
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