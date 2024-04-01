import lib.InputUtil;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Day23 {
    public static void main(String[] args) throws IOException {
        List<Nanobot> nanobots = InputUtil.readAsLines("input23.txt").stream()
                .map(Nanobot::new)
                .collect(Collectors.toList());
        Nanobot largest = nanobots.stream().max(Comparator.comparing(n -> n.r)).orElseThrow();
        long count = nanobots.stream().mapToInt(largest::manhattan).filter(r -> r <= largest.r).count();
        System.out.println(count);
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
    }
}
