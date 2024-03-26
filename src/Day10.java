import lib.InputUtil;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Day10 {
    public static void main(String[] args) throws IOException {
        List<Particle> particles = InputUtil.readAsLines("input10.txt").stream()
                .map(Particle::new)
                .collect(Collectors.toList());
        long lastSurface = surface(particles);
        long count = 0;
        do {
            List<Particle> next = evolve(particles);
            long nextSurface = surface(next);
            if (nextSurface > lastSurface) {
                break;
            }
            count++;
            particles = next;
            lastSurface = nextSurface;
        } while (true);
        print(particles);
        System.out.println(count);
    }

    static List<Particle> evolve(List<Particle> particles) {
        return particles.stream().map(Particle::evolve).collect(Collectors.toList());
    }

    static long surface(List<Particle> particles) {
        long xmin = particles.stream().mapToLong(p -> p.x).min().orElseThrow();
        long xmax = particles.stream().mapToLong(p -> p.x).max().orElseThrow();
        long ymin = particles.stream().mapToLong(p -> p.y).min().orElseThrow();
        long ymax = particles.stream().mapToLong(p -> p.y).max().orElseThrow();
        long w = (xmax - xmin) + 1;
        long h = (ymax - ymin) + 1;
        return w * h;
    }

    static void print(List<Particle> particles) {
        long xmin = particles.stream().mapToLong(p -> p.x).min().orElseThrow();
        long xmax = particles.stream().mapToLong(p -> p.x).max().orElseThrow();
        long ymin = particles.stream().mapToLong(p -> p.y).min().orElseThrow();
        long ymax = particles.stream().mapToLong(p -> p.y).max().orElseThrow();
        for (long y = ymin; y <= ymax; y++) {
            for (long x = xmin; x <= xmax; x++) {
                long xx = x;
                long yy = y;
                boolean set = particles.stream()
                        .anyMatch(p -> p.x == xx && p.y == yy);
                System.out.print(set ? '#' : '.');
            }
            System.out.println();
        }
    }

    static class Particle {
        final long x;
        final long y;
        final long vx;
        final long vy;

        public Particle(long x, long y, long vx, long vy) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
        }

        public Particle(String s) {
            List<String> parts = InputUtil.extract(s, "-?\\d+");
            x = Long.parseLong(parts.get(0));
            y = Long.parseLong(parts.get(1));
            vx = Long.parseLong(parts.get(2));
            vy = Long.parseLong(parts.get(3));
        }

        public Particle evolve() {
            return new Particle(x + vx, y + vy, vx, vy);
        }
    }
}
