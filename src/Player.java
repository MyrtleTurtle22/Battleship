import java.util.Arrays;

public class Player {
    private final String name;
    private final char[][] shipBoard;
    private final char[][] attackBoard;
    Ship bship;
    Ship ac;
    Ship sub;
    Ship cru;
    Ship des;
    int shipsSunk = 0;

    public Player(String name) {
        this.name = name;
        shipBoard = new char[10][10];
        attackBoard = new char[10][10];
        initializeArrays(shipBoard);
        initializeArrays(attackBoard);

        bship = new Ship(5, "Battleship");
        ac = new Ship(4, "Aircraft Carrier");
        sub = new Ship(3, "Submarine");
        cru = new Ship(3, "Cruiser");
        des = new Ship(2, "Destroyer");
    }

    private void initializeArrays(char[][] board) {
        for (char[] chars : board) {
            Arrays.fill(chars, '_');
        }
    }

    public String getName() {
        return name;
    }

    public char[][] getShipBoard() {
        return shipBoard;
    }

    public char[][] getAttackBoard() {
        return attackBoard;
    }

    public int getShipsSunk() {
        return shipsSunk;
    }

    public void setShipsSunk(int sunk) {
        shipsSunk = sunk;
    }
}
