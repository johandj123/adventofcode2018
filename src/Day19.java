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
        System.out.println(computer.registers[0]);
    }

    private static void second(int ipRegister, List<String> instructions) {
        // Running the program is too slow
//        Computer computer = new Computer(ipRegister, instructions);
//        computer.registers[0] = 1;
//        computer.run();
//        System.out.println(computer.registers[0]);
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

    static class Computer {
        final int ipRegister;
        final List<String> instructions;
        final int[] registers = new int[6];

        public Computer(int ipRegister, List<String> instructions) {
            this.ipRegister = ipRegister;
            this.instructions = instructions;
        }

        public void run() {
            int ip = 0;
            while (0 <= ip && ip < instructions.size()) {
                registers[ipRegister] = ip;
                String[] sp = instructions.get(ip).split(" ");
                String mnemonic = sp[0];
                int src1 = Integer.parseInt(sp[1]);
                int src2 = Integer.parseInt(sp[2]);
                int dest = Integer.parseInt(sp[3]);
                switch (mnemonic) {
                    case "addr":
                        registers[dest] = registers[src1] + registers[src2];
                        break;
                    case "addi":
                        registers[dest] = registers[src1] + src2;
                        break;
                    case "mulr":
                        registers[dest] = registers[src1] * registers[src2];
                        break;
                    case "muli":
                        registers[dest] = registers[src1] * src2;
                        break;
                    case "banr":
                        registers[dest] = registers[src1] & registers[src2];
                        break;
                    case "bani":
                        registers[dest] = registers[src1] & src2;
                        break;
                    case "borr":
                        registers[dest] = registers[src1] | registers[src2];
                        break;
                    case "bori":
                        registers[dest] = registers[src1] | src2;
                        break;
                    case "setr":
                        registers[dest] = registers[src1];
                        break;
                    case "seti":
                        registers[dest] = src1;
                        break;
                    case "gtir":
                        registers[dest] = (src1 > registers[src2] ? 1 : 0);
                        break;
                    case "gtri":
                        registers[dest] = (registers[src1] > src2 ? 1 : 0);
                        break;
                    case "gtrr":
                        registers[dest] = (registers[src1] > registers[src2] ? 1 : 0);
                        break;
                    case "eqir":
                        registers[dest] = (src1 == registers[src2] ? 1 : 0);
                        break;
                    case "eqri":
                        registers[dest] = (registers[src1] == src2 ? 1 : 0);
                        break;
                    case "eqrr":
                        registers[dest] = (registers[src1] == registers[src2] ? 1 : 0);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown mnemonic " + mnemonic);
                }
                ip = registers[ipRegister];
                ip++;
            }
        }
    }
}
