import lib.Graph;
import lib.InputUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day25 {
    public static void main(String[] args) throws IOException {
        List<Point> points = InputUtil.readAsLines("input25.txt").stream()
                .map(Point::new)
                .collect(Collectors.toList());
        Graph<Point> graph = new Graph<>();
        for (int i = 0; i < points.size(); i++) {
            Point a = points.get(i);
            graph.addNode(a);
            for (int j = i + 1; j < points.size(); j++) {
                Point b = points.get(j);
                if (a.sameConstellatiation(b)) {
                    graph.addLinkBidirectional(a, b);
                }
            }
        }
        var c = graph.components();
        System.out.println(c.size());
    }

    static class Point {
        int[] x = new int[4];

        public Point(String s) {
            String[] sp = s.split(",");
            for (int i = 0; i < sp.length; i++) {
                x[i] = Integer.parseInt(sp[i].trim());
            }
        }

        public boolean sameConstellatiation(Point o) {
            int s = 0;
            for (int i = 0; i < x.length; i++) {
                s += Math.abs(x[i] - o.x[i]);
            }
            return s <= 3;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return Arrays.equals(x, point.x);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(x);
        }
    }
}
