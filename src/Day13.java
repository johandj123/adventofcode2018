import lib.CharMatrix;
import lib.InputUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Day13 {
    public static void main(String[] args) throws IOException {
        CharMatrix charMatrix = new CharMatrix(InputUtil.readAsLines("input13.txt"));
        first(charMatrix);
        second(charMatrix);
    }

    private static List<Cart> createCarts(CharMatrix charMatrix) {
        List<Cart> carts = new ArrayList<>();
        for (int y = 0; y < charMatrix.getHeight(); y++) {
            for (int x = 0; x < charMatrix.getWidth(); x++) {
                CharMatrix.Position position = charMatrix.new Position(x, y);
                char c = position.get();
                if (c == '^') {
                    carts.add(new Cart(position, new Direction(0, -1)));
                } else if (c == 'v') {
                    carts.add(new Cart(position, new Direction(0, 1)));
                } else if (c == '<') {
                    carts.add(new Cart(position, new Direction(-1, 0)));
                } else if (c == '>') {
                    carts.add(new Cart(position, new Direction(1, 0)));
                }
            }
        }
        return carts;
    }

    private static void first(CharMatrix charMatrix) {
        List<Cart> carts = createCarts(charMatrix);
        while (true) {
            carts.sort(Comparator.comparing((Cart c) -> c.position.getY()).thenComparing(c -> c.position.getX()));
            for (Cart cart : carts) {
                CharMatrix.Position next = cart.next();
                if (cartAtPosition(carts, next)) {
                    System.out.println(next.getX() + "," + next.getY());
                    return;
                }
                cart.advance();
            }
        }
    }

    private static void second(CharMatrix charMatrix) {
        List<Cart> carts = createCarts(charMatrix);
        while (carts.stream().filter(c -> !c.removed).count() > 1) {
            carts.sort(Comparator.comparing((Cart c) -> c.position.getY()).thenComparing(c -> c.position.getX()));
            for (Cart cart : carts) {
                if (cart.removed) {
                    continue;
                }
                CharMatrix.Position next = cart.next();
                if (cartAtPosition(carts, next)) {
                    cart.removed = true;
                    continue;
                }
                cart.advance();
            }
        }
        CharMatrix.Position position = carts.stream().filter(c -> !c.removed).findFirst().orElseThrow().position;
        System.out.println(position.getX() + "," + position.getY());
    }

    static boolean cartAtPosition(List<Cart> carts, CharMatrix.Position position) {
        for (Cart cart : carts) {
            if (!cart.removed && cart.position.equals(position)) {
                cart.removed = true;
                return true;
            }
        }
        return false;
    }

    static class Direction {
        final int x;
        final int y;

        public Direction(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public CharMatrix.Position apply(CharMatrix.Position position) {
            return position.add(x, y);
        }

        public Direction turnLeft() {
            return new Direction(y, -x);
        }

        public Direction turnRight() {
            return new Direction(-y, x);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Direction direction = (Direction) o;
            return x == direction.x && y == direction.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    static class Cart {
        CharMatrix.Position position;
        Direction direction;
        int option = 0;
        boolean removed = false;

        public Cart(CharMatrix.Position position, Direction direction) {
            this.position = position;
            this.direction = direction;
        }

        public void advance() {
            position = next();
            char c = position.get();
            if (c == '+') {
                if (option == 0) {
                    direction = direction.turnLeft();
                } else if (option == 2) {
                    direction = direction.turnRight();
                }
                option = (option + 1) % 3;
            } else if (c == '/') {
                direction = direction.x != 0 ? direction.turnLeft() : direction.turnRight();
            } else if (c == '\\') {
                direction = direction.x != 0 ? direction.turnRight() : direction.turnLeft();
            }
        }

        private CharMatrix.Position next() {
            return direction.apply(position);
        }
    }
}
