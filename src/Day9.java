import lib.InputUtil;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class Day9 {
    public static void main(String[] args) throws IOException {
        List<Integer> input = InputUtil.extractPositiveIntegers(InputUtil.readAsString("input9.txt"));
        int players = input.get(0);
        int lastMarble = input.get(1);
        play(players, lastMarble);
        play(players, lastMarble * 100);
    }

    private static void play(int players, int lastMarble) {
        Deque<Integer> deque = new ArrayDeque<>(List.of(0));
        int turn = 0;
        int currentMarble = 1;
        long[] score = new long[players];
        while (currentMarble <= lastMarble) {
            if ((currentMarble % 23) != 0) {
                deque.addLast(deque.removeFirst());
                deque.addLast(deque.removeFirst());
                deque.addFirst(currentMarble);
            } else {
                score[turn] += currentMarble;
                for (int i = 0; i < 7; i++) {
                    deque.addFirst(deque.removeLast());
                }
                score[turn] += deque.removeFirst();
            }
            turn = (turn + 1) % players;
            currentMarble++;
        }
        long maxScore = Arrays.stream(score).max().orElseThrow();
        System.out.println(maxScore);
    }
}
