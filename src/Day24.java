import lib.InputUtil;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day24 {
    public static void main(String[] args) throws IOException {
        List<String> input = InputUtil.readAsStringGroups("input24.txt");
        List<Group> groups = new ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            String[] sp = input.get(i).split("\n");
            for (int j = 1; j < sp.length; j++) {
                groups.add(new Group(i, sp[j]));
            }
        }
        while (groups.stream().map(g -> g.army).collect(Collectors.toSet()).size() > 1) {
            Map<Group, Group> targetMap = selectTargets(groups);
            attack(targetMap);
            groups.removeIf(Group::isDead);
        }
        int count = groups.stream().mapToInt(g -> g.count).sum();
        System.out.println(count);
    }

    private static Map<Group, Group> selectTargets(List<Group> groups) {
        groups.forEach(g -> g.underAttack = false);
        Map<Group, Group> result = new HashMap<>();
        List<Group> groupsInOrder = new ArrayList<>(groups);
        groupsInOrder.sort(Comparator.comparing(Group::effectivePower).thenComparing(g -> g.initiative).reversed());
        for (Group group : groupsInOrder) {
            List<Group> possibleTargets = groups.stream()
                    .filter(target -> !target.underAttack)
                    .filter(target -> target.army != group.army)
                    .filter(target -> !target.immune.contains(group.attackType))
                    .collect(Collectors.toList());
            Optional<Group> targetOptional = possibleTargets.stream()
                    .max(Comparator.comparing(group::calculateDamage).thenComparing(Group::effectivePower).thenComparing(g -> g.initiative));
            targetOptional.ifPresent(target -> {
                result.put(group, target);
                target.underAttack = true;
            });
        }
        return result;
    }

    private static void attack(Map<Group, Group> targetMap) {
        List<Group> groupsInOrder = new ArrayList<>(targetMap.keySet());
        groupsInOrder.sort(Comparator.comparing((Group g) -> g.initiative).reversed());
        for (Group attacker : groupsInOrder) {
            if (attacker.isAlive()) {
                Group defender = targetMap.get(attacker);
                if (defender.isAlive()) {
                    int damage = attacker.calculateDamage(defender);
                    int kills = damage / defender.hitPoints;
                    defender.count -= kills;
                }
            }
        }
    }

    static class Group {
        final int army;
        int count;
        final int hitPoints;
        final Set<String> immune = new HashSet<>();
        final Set<String> weak = new HashSet<>();
        final int attackDamage;
        String attackType;
        final int initiative;
        boolean underAttack;

        public Group(int army, String s) {
            this.army = army;
            List<Integer> list = InputUtil.extractPositiveIntegers(s);
            count = list.get(0);
            hitPoints = list.get(1);
            attackDamage = list.get(2);
            initiative = list.get(3);
            List<String> part = InputUtil.extract(s, "\\([^\\)]+\\)");
            if (!part.isEmpty()) {
                String ss = part.get(0).replace("to", "").replace(",", "").replace(";", "").replace("(", "").replace(")", "");
                String[] sp = ss.split(" +");
                Set<String> set = null;
                for (String sss : sp) {
                    if ("weak".equals(sss)) {
                        set = weak;
                    } else if ("immune".equals(sss)) {
                        set = immune;
                    } else {
                        set.add(sss);
                    }
                }
            }
            String[] sp = s.split(" ");
            for (int i = 0; i < sp.length; i++) {
                if ("damage".equals(sp[i])) {
                    attackType = sp[i - 1];
                }
            }
        }

        public boolean isAlive() {
            return count > 0;
        }

        public boolean isDead() {
            return !isAlive();
        }

        public int effectivePower() {
            return count * attackDamage;
        }

        public int calculateDamage(Group defender) {
            if (defender.immune.contains(attackType)) {
                return 0;
            }
            int damage = effectivePower();
            if (defender.weak.contains(attackType)) {
                return damage * 2;
            } else {
                return damage;
            }
        }

        @Override
        public String toString() {
            return String.format("Army %d Unit count %d", army, count);
        }
    }
}
