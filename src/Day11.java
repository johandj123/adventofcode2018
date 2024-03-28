import lib.InputUtil;

import java.io.IOException;

public class Day11 {
    public static void main(String[] args) throws IOException {
        int serial = Integer.parseInt(InputUtil.readAsString("input11.txt"));
        first(serial);
        second(serial);
    }

    private static void first(int serial) {
        int maxValue = Integer.MIN_VALUE;
        int maxX = 0;
        int maxY = 0;
        for (int y = 0; y < 300 - 2; y++) {
            for (int x = 0; x < 300 - 2; x++) {
                int value = 0;
                for (int dy = 0; dy < 3; dy++) {
                    for (int dx = 0; dx < 3; dx++) {
                        value += power(x + dx, y + dy, serial);
                    }
                }
                if (value > maxValue) {
                    maxValue = value;
                    maxX = x;
                    maxY = y;
                }
            }
        }
        System.out.println(maxX + "," + maxY);
    }

    private static void second(int serial) {
        int maxValue = Integer.MIN_VALUE;
        int maxX = 0;
        int maxY = 0;
        int maxSize = 0;
        for (int y = 0; y < 300; y++) {
            for (int x = 0; x < 300; x++) {
                int value = 0;
                for (int size = 1; size <= Math.min(300 - x, 300 - y); size++) {
                    value += power(x + size - 1, y + size - 1, serial);
                    for (int i = 0; i < size - 1; i++) {
                        value += power(x + i, y + size - 1, serial);
                        value += power(x + size - 1, y + i, serial);
                    }
                    if (value > maxValue) {
                        maxValue = value;
                        maxX = x;
                        maxY = y;
                        maxSize = size;
                    }
                }
            }
        }
        System.out.println(maxX + "," + maxY + "," + maxSize);
    }

    private static int power(int x,int y,int serial) {
        int rackId = x + 10;
        int power = rackId * y;
        power += serial;
        power *= rackId;
        power = ((power % 1000) / 100);
        return power - 5;
    }
}
