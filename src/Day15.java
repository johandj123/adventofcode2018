import lib.CharMatrix;
import lib.InputUtil;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day15 {
    public static void main(String[] args) throws IOException {
        CharMatrix charMatrix = new CharMatrix(InputUtil.readAsLines("input15.txt"));
        List<Unit> units = new ArrayList<>();
        for (int y = 0; y < charMatrix.getHeight(); y++) {
            for (int x = 0; x < charMatrix.getWidth(); x++) {
                CharMatrix.Position position = charMatrix.new Position(x, y);
                char c = position.get();
                if (c == 'E') {
                    units.add(new Unit(UnitType.ELF, position));
                } else if (c == 'G') {
                    units.add(new Unit(UnitType.GOBLIN, position));
                }
            }
        }
        int rounds = 0;
        outer:
        while (true) {
            units.sort(null);
            for (Unit unit : units) {
                if (unit.isDead()) {
                    continue;
                }
                List<Unit> enemies = units.stream().filter(Unit::isAlive).filter(u -> u.unitType != unit.unitType).collect(Collectors.toList());
                if (enemies.isEmpty()) {
                    break outer;
                }
                CharMatrix.Position position = determineMovePosition(unit, enemies);
                if (!unit.position.equals(position)) {
                    position.set(unit.position.get());
                    unit.position.set('.');
                    unit.position = position;
                }
                Map<CharMatrix.Position, Unit> map = enemies.stream().collect(Collectors.toMap(u -> u.position, u -> u));
                Unit enemy = position.getNeighbours().stream()
                        .map(map::get)
                        .filter(Objects::nonNull)
                        .min(Comparator.comparing((Unit u) -> u.hitPoints).thenComparing(u -> u.position))
                        .orElse(null);
                if (enemy != null) {
                    unit.attack(enemy);
                    if (enemy.isDead()) {
                        enemy.position.set('.');
                    }
                }
            }
            rounds++;
        }
        int hitpointsLeft = units.stream().filter(Unit::isAlive).mapToInt(u -> u.hitPoints).sum();
        System.out.println(rounds * hitpointsLeft);
    }

    static private CharMatrix.Position determineMovePosition(Unit unit, List<Unit> enemies) {
        Set<CharMatrix.Position> done = new HashSet<>(List.of(unit.position));
        Set<CharMatrix.Position> current = new HashSet<>(List.of(unit.position));
        Set<CharMatrix.Position> enemyPositions = enemies.stream().map(p -> p.position).collect(Collectors.toSet());
        Map<CharMatrix.Position, CharMatrix.Position> parent = new HashMap<>();
        while (!current.isEmpty()) {
            Set<CharMatrix.Position> next = new HashSet<>();
            List<CharMatrix.Position> currentSorted = new ArrayList<>(current);
            currentSorted.sort(null);
            for (var position : currentSorted) {
                List<CharMatrix.Position> neighbours = position.getNeighbours();
                neighbours.sort(null);
                for (var neighbour : neighbours) {
                    if (!done.contains(neighbour)) {
                        if (neighbour.get() == '.') {
                            done.add(neighbour);
                            next.add(neighbour);
                            parent.put(neighbour, position);
                        }
                        if (enemyPositions.contains(neighbour)) {
                            CharMatrix.Position result = position;
                            while (!result.equals(unit.position) && !parent.get(result).equals(unit.position)) {
                                result = parent.get(result);
                            }
                            return result;
                        }
                    }
                }
            }
            current = next;
        }
        return unit.position;
    }

    static class Unit implements Comparable<Unit> {
        final UnitType unitType;
        CharMatrix.Position position;
        int attackPower = 3;
        int hitPoints = 200;

        public Unit(UnitType unitType, CharMatrix.Position position) {
            this.unitType = unitType;
            this.position = position;
        }

        public void attack(Unit o) {
            o.hitPoints -= attackPower;
        }

        public boolean isDead() {
            return hitPoints <= 0;
        }

        public boolean isAlive() {
            return !isDead();
        }

        @Override
        public int compareTo(Unit o) {
            return position.compareTo(o.position);
        }

        @Override
        public String toString() {
            return (isDead() ? "dead " : "") + unitType + " " + position + " " + attackPower + "/" + hitPoints;
        }
    }

    enum UnitType {
        ELF,
        GOBLIN
    }
}
