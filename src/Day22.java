import lib.GraphUtil;
import lib.InputUtil;

import java.io.IOException;
import java.util.*;

public class Day22 {
    private int depth;
    private Position target;
    private final Map<Position, Long> geologicIndexCache = new HashMap<>();

    private static final int NONE = 0;
    private static final int TORCH = 1;
    private static final int CLIMBING_GEAR = 2;
    private static final List<Position> DIRECTIONS = List.of(new Position(-1, 0), new Position(1, 0), new Position(0, -1), new Position(0, 1));
    private static final Comparator<Node> NODE_COMPARATOR = Comparator.comparing((Node node) -> node.position.y).thenComparing(node -> node.position.x).thenComparing(node -> node.tool);

    public static void main(String[] args) throws IOException {
        new Day22().start();
    }

    private void start() throws IOException {
        List<Integer> input = InputUtil.extractPositiveIntegers(InputUtil.readAsString("input22.txt"));
        depth = input.get(0);
        target = new Position(input.get(1), input.get(2));
        first();
        second();
    }

    private void first() {
        int sum = 0;
        for (int y = 0; y <= target.y; y++) {
            for (int x = 0; x <= target.x; x++) {
                int type = type(new Position(x, y));
                System.out.print(".=|".charAt(type));
                sum += type;
            }
            System.out.println();
        }
        System.out.println(sum);
    }

    private void second() {
        Node startNode = new Node(new Position(0, 0), TORCH);
        int distance = GraphUtil.dijkstra(startNode, Node::getNeighbours, Node::isEnd);
        System.out.println(distance);
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
        if (position.equals(target)) {
            return 0;
        }
        long e = erosionLevel(position);
        return (int)(e % 3);
    }

    class Node implements Comparable<Node> {
        final Position position;
        final int tool;

        public Node(Position position, int tool) {
            this.position = position;
            this.tool = tool;
        }

        public Map<Node, Integer> getNeighbours() {
            Map<Node, Integer> result = new HashMap<>();
            int type = type(position);
            result.put(new Node(position, 3 - type - tool), 7);
            for (Position direction : DIRECTIONS) {
                Position next = position.add(direction);
                if (next.x >= 0 && next.y >= 0 && next.x <= target.x * 2 && next.y <= target.y * 2) {
                    int nextType = type(next);
                    if (nextType != tool) {
                        result.put(new Node(next, tool), 1);
                    }
                }
            }
            return result;
        }

        public boolean isEnd() {
            return tool == TORCH && position.equals(target);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return tool == node.tool && Objects.equals(position, node.position);
        }

        @Override
        public int hashCode() {
            return Objects.hash(position, tool);
        }

        @Override
        public int compareTo(Node o) {
            return NODE_COMPARATOR.compare(this, o);
        }
    }

    static class Position {
        final int x;
        final int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Position add(Position o) {
            return new Position(x + o.x, y + o.y);
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
