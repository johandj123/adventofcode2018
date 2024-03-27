import lib.Counter;
import lib.InputUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day4 {
    public static void main(String[] args) throws IOException {
        List<Record> input = readInput();
        Map<Integer, Guard> guardMap = groupByGuard(input);
        first(guardMap);
        second(guardMap);
    }

    private static List<Record> readInput() throws IOException {
        return InputUtil.readAsLines("input4.txt").stream()
                .map(Record::new)
                .sorted()
                .collect(Collectors.toList());
    }

    private static Map<Integer, Guard> groupByGuard(List<Record> input) {
        Map<Integer, Guard> guardMap = new HashMap<>();
        Guard guard = null;
        LocalDateTime asleepLocalDateTime = null;
        for (Record record : input) {
            if (record.message.contains("Guard")) {
                int id = InputUtil.extractPositiveIntegers(record.message).get(0);
                guard = guardMap.computeIfAbsent(id, key -> new Guard(id));
                asleepLocalDateTime = null;
            } else if (record.message.contains("falls asleep")) {
                asleepLocalDateTime = record.localDateTime;
            } else if (record.message.contains("wakes up")) {
                guard.sleep(asleepLocalDateTime, record.localDateTime);
                asleepLocalDateTime = null;
            } else {
                throw new IllegalArgumentException("Could not process message " + record.message);
            }
        }
        return guardMap;
    }

    private static void first(Map<Integer, Guard> guardMap) {
        Guard sleepiestGuard = guardMap.values().stream()
                .max(Comparator.comparing(g -> g.minutesAsleep))
                .orElseThrow();
        int sleepiestMinute = sleepiestGuard.sleepiestMinute();
        System.out.println(sleepiestGuard.id * sleepiestMinute);
    }

    private static void second(Map<Integer, Guard> guardMap) {
        Guard chosenGuard = null;
        int minute = 0;
        int maxTimesAsleep = 0;
        for (Guard guard : guardMap.values()) {
            for (var entry : guard.minutesAsleepCounter.entrySet()) {
                if (entry.getValue() > maxTimesAsleep) {
                    chosenGuard = guard;
                    minute = entry.getKey();
                    maxTimesAsleep = entry.getValue();
                }
            }
        }
        System.out.println(chosenGuard.id * minute);
    }

    static class Record implements Comparable<Record> {
        private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        final LocalDateTime localDateTime;
        final String message;

        public Record(String s) {
            int index = s.indexOf(']');
            localDateTime = LocalDateTime.parse(s.substring(1, index), DATE_TIME_FORMAT);
            message = s.substring(index + 2);
        }

        @Override
        public int compareTo(Record o) {
            return localDateTime.compareTo(o.localDateTime);
        }

        @Override
        public String toString() {
            return localDateTime + " | " + message;
        }
    }

    static class Guard {
        final int id;
        long minutesAsleep = 0;
        Counter<Integer> minutesAsleepCounter = new Counter<>();

        public Guard(int id) {
            this.id = id;
        }

        public void sleep(LocalDateTime start, LocalDateTime end) {
            minutesAsleep += start.until(end, ChronoUnit.MINUTES);
            for (int i = start.getMinute(); i < end.getMinute(); i++) {
                minutesAsleepCounter.inc(i);
            }
        }

        public int sleepiestMinute() {
            return minutesAsleepCounter.maxCount().getKey();
        }
    }
}
