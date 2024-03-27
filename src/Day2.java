import lib.CollectionUtil;
import lib.Counter;
import lib.InputUtil;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Day2 {
    public static void main(String[] args) throws IOException {
        List<String> input = InputUtil.readAsLines("input2.txt");
        first(input);
        second(input);
    }

    private static void first(List<String> input) {
        int two = 0;
        int three = 0;
        for (String s : input) {
            Counter<Integer> h = CollectionUtil.calculateHistogram(s.codePoints().boxed().collect(Collectors.toList()));
            if (h.values().contains(2)) {
                two++;
            }
            if (h.values().contains(3)) {
                three++;
            }
        }
        System.out.println(two * three);
    }

    private static void second(List<String> input) {
        for (int i = 0; i < input.size(); i++) {
            for (int j = i + 1; j < input.size(); j++) {
                String c = common(input.get(i), input.get(j));
                if (c != null) {
                    System.out.println(c);
                }
            }
        }
    }

    private static String common(String s,String t) {
        int count = 0;
        int pos = 0;
        if (s.length() != t.length()) {
            return null;
        }
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != t.charAt(i)) {
                count++;
                pos = i;
            }
        }
        if (count != 1) {
            return null;
        }
        return s.substring(0, pos) + s.substring(pos + 1);
    }
}
