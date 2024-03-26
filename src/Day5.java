import lib.InputUtil;

import java.io.IOException;

public class Day5 {
    public static void main(String[] args) throws IOException {
        String input = InputUtil.readAsString("input5.txt");
        first(input);
        second(input);
    }

    private static void first(String input) {
        System.out.println(collapse(input).length());
    }

    private static void second(String input) {
        int minLength = Integer.MAX_VALUE;
        for (char c = 'a'; c <= 'z'; c++) {
            int length = collapse(remove(input, c)).length();
            if (length < minLength) {
                minLength = length;
            }
        }
        System.out.println(minLength);
    }

    private static String collapse(String input) {
        String current = input;
        while (true) {
            String next = step(current);
            if (next.equals(current)) {
                break;
            }
            current = next;
        }
        return current;
    }

    private static String remove(String input, char cr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == cr || c == (cr - 'a' + 'A')) {
                continue;
            }
            sb.append(c);
        }
        return sb.toString();
    }

    private static String step(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (i == s.length() - 1) {
                sb.append(c);
                break;
            }
            char d = s.charAt(i + 1);
            if (c >= 'a' && c <= 'z' && d == (c - 'a' + 'A')) {
                i++;
            } else if (c >= 'A' && c <= 'Z' && d == (c - 'A' + 'a')) {
                i++;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
