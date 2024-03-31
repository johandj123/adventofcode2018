import lib.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day18 {
    public static void main(String[] args) throws IOException {
        CharMatrix charMatrix = new CharMatrix(InputUtil.readAsLines("input18.txt"));
        first(charMatrix);
        second(charMatrix);
    }

    private static void first(CharMatrix charMatrix) {
        for (int i = 0; i < 10; i++) {
            charMatrix = evolve(charMatrix);
        }
        printResult(charMatrix);
    }

    private static void second(CharMatrix charMatrix) {
        charMatrix = StepUtil.performStepsWithCycleDetection(charMatrix, 1000000000, Day18::evolve);
        printResult(charMatrix);
    }

    private static void printResult(CharMatrix charMatrix) {
        List<Character> characters = new ArrayList<>();
        for (int y = 0; y < charMatrix.getHeight(); y++) {
            for (int x = 0; x < charMatrix.getWidth(); x++) {
                characters.add(charMatrix.get(x, y));
            }
        }
        Counter<Character> counter = CollectionUtil.calculateHistogram(characters);
        System.out.println(counter.get('|') * counter.get('#'));
    }

    private static CharMatrix evolve(CharMatrix charMatrix) {
        CharMatrix result = new CharMatrix(charMatrix.getHeight(), charMatrix.getWidth());
        for (int y = 0; y < charMatrix.getHeight(); y++) {
            for (int x = 0; x < charMatrix.getWidth(); x++) {
                CharMatrix.Position position = charMatrix.new Position(x, y);
                CharMatrix.Position dest = result.new Position(x, y);
                Counter<Character> neighbours = CollectionUtil.calculateHistogram(position.getNeighboursIncludingDiagonal().stream()
                        .map(CharMatrix.Position::get)
                        .collect(Collectors.toList()));
                switch (position.get()) {
                    case '.':
                        dest.set(neighbours.get('|') >= 3 ? '|' : '.');
                        break;
                    case '|':
                        dest.set(neighbours.get('#') >= 3 ? '#' : '|');
                        break;
                    case '#':
                        dest.set((neighbours.get('#') >= 1 && neighbours.get('|') >= 1) ? '#' : '.');
                        break;
                }
            }
        }
        return result;
    }
}
