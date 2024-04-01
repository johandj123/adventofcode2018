import lib.InputUtil;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class Day23 {
    public static void main(String[] args) throws IOException {
        List<Nanobot> nanobots = InputUtil.readAsLines("input23.txt").stream()
                .map(Nanobot::new)
                .collect(Collectors.toList());
        first(nanobots);
        second(nanobots);
    }

    private static void first(List<Nanobot> nanobots) {
        Nanobot largest = nanobots.stream().max(Comparator.comparing(n -> n.r)).orElseThrow();
        long count = nanobots.stream().mapToInt(largest::manhattan).filter(r -> r <= largest.r).count();
        System.out.println(count);
    }

    private static void second(List<Nanobot> nanobots) {
        PriorityQueue<Border> borders = new PriorityQueue<>();
        for (Nanobot nanobot : nanobots) {
            int d = nanobot.manhattanDistaneToOrigin();
            borders.add(new Border(Math.max(0, d - nanobot.r), 1));
            borders.add(new Border(d + nanobot.r + 1, -1));
        }
        int currentCount = 0;
        int maxCount = 0;
        int result = 0;
        while (!borders.isEmpty()) {
            Border border = borders.remove();
            currentCount += border.startOrEnd;
            if (currentCount > maxCount) {
                result = border.distance;
                maxCount = currentCount;
            }
        }
        System.out.println(result);
    }

    static class Nanobot {
        final int[] position = new int[3];
        final int r;

        public Nanobot(String s) {
            List<String> list = InputUtil.extract(s, "-?\\d+");
            for (int i = 0; i < 3; i++) {
                position[i] = Integer.parseInt(list.get(i));
            }
            r = Integer.parseInt(list.get(3));
        }

        public int manhattan(Nanobot o) {
            int result = 0;
            for (int i = 0; i < position.length; i++) {
                result += Math.abs(position[i] - o.position[i]);
            }
            return result;
        }

        public int manhattanDistaneToOrigin() {
            int result = 0;
            for (int i = 0; i < position.length; i++) {
                result += Math.abs(position[i]);
            }
            return result;
        }
    }

    static class Border implements Comparable<Border> {
        final int distance;
        final int startOrEnd;

        public Border(int distance, int startOrEnd) {
            this.distance = distance;
            this.startOrEnd = startOrEnd;
        }

        @Override
        public int compareTo(Border o) {
            return Integer.compare(distance, o.distance);
        }
    }
}
