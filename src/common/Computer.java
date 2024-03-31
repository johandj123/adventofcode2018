package common;

import java.util.List;

public class Computer {
    final int ipRegister;
    final List<String> instructions;
    final int[] registers = new int[6];

    public Computer(int ipRegister, List<String> instructions) {
        this.ipRegister = ipRegister;
        this.instructions = instructions;
    }

    public int[] getRegisters() {
        return registers;
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
