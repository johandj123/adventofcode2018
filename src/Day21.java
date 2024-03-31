import common.Computer;
import lib.InputUtil;

import java.io.IOException;
import java.util.List;

public class Day21 {
    public static void main(String[] args) throws IOException {
        List<String> input = InputUtil.readAsLines("input21.txt");
        int ipRegister = InputUtil.extractPositiveIntegers(input.get(0)).get(0);
        List<String> instructions = input.subList(1, input.size());
        first(ipRegister, instructions);
    }

    private static void first(int ipRegister, List<String> instructions) {
        Computer computer = new Computer(ipRegister, instructions);
        computer.getRegisters()[0] = 1024276;   // Value determined by reading the program
        computer.run();
    }
}
