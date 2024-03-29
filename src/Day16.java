import lib.InputUtil;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;

public class Day16 {
    public static void main(String[] args) throws IOException {
        List<String> input = InputUtil.readAsStringGroups("input16.txt");
        Map<Integer, Set<String>> opcodePossibleMap = new HashMap<>();
        int count = 0;
        for (String s : input) {
            if (s.isBlank()) {
                break;
            }
            String[] sp = s.split("\n");
            List<Integer> before = InputUtil.extractPositiveIntegers(sp[0]);
            List<Integer> instruction = InputUtil.extractPositiveIntegers(sp[1]);
            List<Integer> after = InputUtil.extractPositiveIntegers(sp[2]);
            int opcode = instruction.get(0);
            Set<String> mnemonics = possibleMnemonics(before, instruction, after);
            if (mnemonics.size() >= 3) {
                count++;
            }
            if (!opcodePossibleMap.containsKey(opcode)) {
                opcodePossibleMap.put(opcode, mnemonics);
            } else {
                Set<String> existingMnemonics = opcodePossibleMap.get(opcode);
                existingMnemonics.removeIf(existingMnemonic -> !mnemonics.contains(existingMnemonic));
            }
        }
        System.out.println(count);
        Map<Integer, String> opcodeMap = deduceOpcodeMap(opcodePossibleMap);
        runProgram(opcodeMap, input.get(input.size() - 1));
    }

    private static void runProgram(Map<Integer, String> opcodeMap, String program)
    {
        String[] sp = program.split("\n");
        int[] registers = new int[4];
        for (String s : sp) {
            List<Integer> instruction = InputUtil.extractPositiveIntegers(s);
            String mnemonic = opcodeMap.get(instruction.get(0));
            int src1 = instruction.get(1);
            int src2 = instruction.get(2);
            int dest = instruction.get(3);
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
        }
        System.out.println(registers[0]);
    }

    private static Map<Integer, String> deduceOpcodeMap(Map<Integer, Set<String>> opcodePossibleMap) {
        Map<Integer, String> result = new HashMap<>();
        do {
            int opcode = opcodePossibleMap.entrySet().stream()
                    .filter(e -> e.getValue().size() == 1)
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElseThrow();
            String mnemonic = opcodePossibleMap.get(opcode).iterator().next();
            opcodePossibleMap.remove(opcode);
            for (var set : opcodePossibleMap.values()) {
                set.remove(mnemonic);
            }
            result.put(opcode, mnemonic);
        } while (!opcodePossibleMap.isEmpty());
        return result;
    }

    private static Set<String> possibleMnemonics(List<Integer> before, List<Integer> instruction, List<Integer> after) {
        Map<String, List<Integer>> map = new HashMap<>();
        int value1 = instruction.get(1);
        int value2 = instruction.get(2);
        int dest = instruction.get(3);
        Function<Integer, List<Integer>> modifyList = result -> {
            List<Integer> ml = new ArrayList<>(before);
            ml.set(dest, result);
            return ml;
        };
        int reg1 = before.get(value1);
        int reg2 = before.get(value2);
        map.put("addr", modifyList.apply(reg1 + reg2));
        map.put("addi", modifyList.apply(reg1 + value2));
        map.put("mulr", modifyList.apply(reg1 * reg2));
        map.put("muli", modifyList.apply(reg1 * value2));
        map.put("banr", modifyList.apply(reg1 & reg2));
        map.put("bani", modifyList.apply(reg1 & value2));
        map.put("borr", modifyList.apply(reg1 | reg2));
        map.put("bori", modifyList.apply(reg1 | value2));
        map.put("setr", modifyList.apply(reg1));
        map.put("seti", modifyList.apply(value1));
        map.put("gtir", modifyList.apply(value1 > reg2 ? 1 : 0));
        map.put("gtri", modifyList.apply(reg1 > value2 ? 1 : 0));
        map.put("gtrr", modifyList.apply(reg1 > reg2 ? 1 : 0));
        map.put("eqir", modifyList.apply(value1 == reg2 ? 1 : 0));
        map.put("eqri", modifyList.apply(reg1 == value2 ? 1 : 0));
        map.put("eqrr", modifyList.apply(reg1 == reg2 ? 1 : 0));
        Set<String> result = new HashSet<>();
        for (var entry : map.entrySet()) {
            if (entry.getValue().equals(after)) {
                result.add(entry.getKey());
            }
        }
        return result;
    }
}
