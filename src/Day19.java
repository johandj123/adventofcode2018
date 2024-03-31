import common.Computer;
import lib.InputUtil;

import java.io.IOException;
import java.util.List;

public class Day19 {
    public static void main(String[] args) throws IOException {
        List<String> input = InputUtil.readAsLines("input19.txt");
        int ipRegister = InputUtil.extractPositiveIntegers(input.get(0)).get(0);
        List<String> instructions = input.subList(1, input.size());
        first(ipRegister, instructions);
        second(ipRegister, instructions);
    }

    private static void first(int ipRegister, List<String> instructions) {
        Computer computer = new Computer(ipRegister, instructions);
        computer.run();
        System.out.println(computer.getRegisters()[0]);
    }

    private static void second(int ipRegister, List<String> instructions) {
        // Running the program is too slow
//        Computer computer = new Computer(ipRegister, instructions);
//        computer.getRegisters()[0] = 1;
//        computer.run();
//        System.out.println(computer.getRegisters()[0]);
        // Reverse engineering the program show that it sums all dividers of 10551354 (or 954 for part 1)
        int result = 0;
        int number = 10551354;
        for (int i = 1; i <= number; i++) {
            if ((number % i) == 0) {
                result += i;
            }
        }
        System.out.println(result);
    }
}
