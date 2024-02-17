import java.util.Arrays;

public class Player {
    private String name;
    private char[][] shipBoard;
    private char[][] attackBoard;

    public Player(String name) {
        this.name = name;
        shipBoard = new char[10][10];
        attackBoard = new char[10][10];
        initializeArrays(shipBoard);
        initializeArrays(attackBoard);
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

    public void setShipBoard(char[][] shipBoard) {
        this.shipBoard = shipBoard;
    }

    public void setAttackBoard(char[][] attackBoard) {
        this.attackBoard = attackBoard;
    }
}
