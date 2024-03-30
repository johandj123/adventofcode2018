import lib.InputUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day14 {
    public static void main(String[] args) throws IOException {
        int input = InputUtil.readAsIntegers("input14.txt").get(0);
        first(input);
        second(Integer.toString(input));
    }

    private static void first(int input) {
        List<Integer> list = new ArrayList<>(List.of(3, 7));
        int elf1 = 0;
        int elf2 = 1;
        while (list.size() < input + 10) {
            int sum = list.get(elf1) + list.get(elf2);
            if (sum >= 10) {
                list.add(sum / 10);
            }
            list.add(sum % 10);
            elf1 = (elf1 + 1 + list.get(elf1)) % list.size();
            elf2 = (elf2 + 1 + list.get(elf2)) % list.size();
        }
        List<Integer> list1 = list.subList(input, input + 10);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(list1.get(i));
        }
        String string = sb.toString();
        System.out.println(string);
    }

    private static void second(String input) {
        List<Integer> goal = input.codePoints().mapToObj(x -> x - '0').collect(Collectors.toList());
        List<Integer> list = new ArrayList<>(List.of(3, 7));
        int elf1 = 0;
        int elf2 = 1;
        while (true) {
            int sum = list.get(elf1) + list.get(elf2);
            if (sum >= 10) {
                list.add(sum / 10);
                if (endsWith(list, goal)) {
                    break;
                }
            }
            list.add(sum % 10);
            if (endsWith(list, goal)) {
                break;
            }
            elf1 = (elf1 + 1 + list.get(elf1)) % list.size();
            elf2 = (elf2 + 1 + list.get(elf2)) % list.size();
        }
        System.out.println(list.size() - goal.size());
    }

    private static boolean endsWith(List<Integer> list,List<Integer> goal) {
        if (list.size() < goal.size()) {
            return false;
        }
        List<Integer> tail = list.subList(list.size() - goal.size(), list.size());
        return tail.equals(goal);
    }
}
