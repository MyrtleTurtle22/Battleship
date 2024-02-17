import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class GameLoop {
    Scanner s = new Scanner(System.in);
    boolean gameOver = false;
    ArrayList<Player> players = new ArrayList<>();

    public void gameLoop(Player p1, Player p2) throws InterruptedException, IOException {
        players.add(p1);
        players.add(p2);
        System.out.println(p1.getName() + " will place their ships first.");
        printBoard(p1.getShipBoard());
        placeShip(p1, p1.bship);
        printBoard(p1.getShipBoard());
        placeShip(p1, p1.ac);
        printBoard(p1.getShipBoard());
        placeShip(p1, p1.cru);
        printBoard(p1.getShipBoard());
        placeShip(p1, p1.sub);
        printBoard(p1.getShipBoard());
        placeShip(p1, p1.des);
        printBoard(p1.getShipBoard());
        System.out.println(p1.getName() + ", you have 10 seconds to" +
                " look at your board before " + p2.getName() + " places their ships.");
        TimeUnit.SECONDS.sleep(10);
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

        System.out.println("Now " + p2.getName() + " places their ships.");
        printBoard(p2.getShipBoard());
        placeShip(p2, p2.bship);
        printBoard(p2.getShipBoard());
        placeShip(p2, p2.ac);
        printBoard(p2.getShipBoard());
        placeShip(p2, p2.cru);
        printBoard(p2.getShipBoard());
        placeShip(p2, p2.sub);
        printBoard(p2.getShipBoard());
        placeShip(p2, p2.des);
        printBoard(p2.getShipBoard());

        System.out.println(p2.getName() + ", you have 10 seconds to" +
                " look at your board before the game begins.");
        TimeUnit.SECONDS.sleep(10);
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

        while (!gameOver) {
            attack(p1, p2);
            attack(p2, p1);
        }
    }

    private void placeShip(Player p, Ship ship) {
        int row = 0;
        int col = 0;
        char orientation = 'D';
        boolean valid = false;
        while (!valid) { //user either has not yet entered coordinates or entered incorrect coordinates
            System.out.println("Enter the row number followed by the column number of the top left position" +
                    " of your " + ship.getName() + " separated by a space:");
            row = s.nextInt();
            col = s.nextInt();
            s.nextLine();

            System.out.println("Enter either 'D' or 'R' if you want the ship to face right or down");
            orientation = s.next().charAt(0);
            valid = validateCoordinates(p, ship, row, col, orientation);
        }
        //change each ship position to 'X'
        modifyShipBoard(p, ship, row, col, orientation);
    }

    private boolean validateCoordinates(Player p, Ship s, int row, int col, char orientation) {
        if (orientation == 'R') {
            if (col > (11 - s.getLength())) {
                System.out.println("Invalid position, end of ship would be off the board");
                return false;
            }
            for (int i = 0; i < s.getLength(); i++) {
                if (p.getShipBoard()[row - 1][col + i - 1] != '_') {
                    System.out.println("Spot already taken");
                    return false;
                }
            }
        } else if (orientation == 'D') {
            if (row > (11 - s.getLength())) {
                System.out.println("Invalid position, bottom of ship would be off the board");
                return false;
            }
            for (int i = 0; i < s.getLength(); i++) {
                if (p.getShipBoard()[row + i - 1][col - 1] != '_') {
                    System.out.println("Spot already taken");
                    return false;
                }
            }
        }

        return true;
    }

    private void modifyShipBoard(Player p, Ship s, int row, int col, char orientation) {
        //arrays are zero indexed but players will use 1 indexing
        if (orientation == 'R') {
            for (int i = 0; i < s.getLength(); i++) {
                p.getShipBoard()[row - 1][col + i - 1] = s.getName().charAt(0);
            }
        } else {
            for (int i = 0; i < s.getLength(); i++) {
                p.getShipBoard()[row + i - 1][col - 1] = s.getName().charAt(0);
            }
        }
    }

    private void printBoard(char[][] board) {
        System.out.printf("%-4s", "");
        for (int i = 0; i < board[0].length; i++) {
            System.out.printf("%-4d", i + 1);
        }
        System.out.println();
        for (int i = 0; i < board.length; i++) {
            System.out.printf("%-4d", i + 1);
            for (int j = 0; j < board[0].length; j++) {
                System.out.printf("%-4c", board[i][j]);
            }
            System.out.println();
        }

    }

    private void attack(Player attacker, Player defender) {
        boolean valid = false;
        int row = 0;
        int col = 0;
        printBoard(attacker.getAttackBoard());
        while (!valid) {
            System.out.println(attacker.getName() + ", enter the row number followed by the column number" +
                    " of the target you wish to attack");
            row = s.nextInt() - 1;
            col = s.nextInt() - 1;
            if (attacker.getAttackBoard()[row][col] == '_') {
                valid = true;
            } else {
                System.out.println("You already attacked this spot, enter a different spot.");
            }
        }

        if (defender.getShipBoard()[row][col] != '_') { //hit
            attacker.getAttackBoard()[row][col] = 'X'; // update attacker board
            //check if sink ship
            System.out.println(defender.getShipBoard()[row][col]);
            switch (defender.getShipBoard()[row][col]) {
                case 'B':
                    System.out.println("Hit! Battleship!");
                    defender.bship.setTimesHit(defender.bship.getTimesHit() + 1);
                    if (defender.bship.getTimesHit() == 5) {
                        attacker.setShipsSunk(attacker.getShipsSunk() + 1);
                        System.out.println(attacker.getName() + " has sunk " + defender.getName() + "'s Battleship!");
                    }
                case 'A':
                    System.out.println("Hit! Aircraft Carrier!");
                    defender.ac.setTimesHit(defender.ac.getTimesHit() + 1);
                    if (defender.ac.getTimesHit() == 4) {
                        attacker.setShipsSunk(attacker.getShipsSunk() + 1);
                        System.out.println(attacker.getName() + " has sunk " + defender.getName() + "'s Aircraft Carrier!");
                    }
                case 'C':
                    System.out.println("Hit! Cruiser!");
                    defender.cru.setTimesHit(defender.cru.getTimesHit() + 1);
                    if (defender.cru.getTimesHit() == 3) {
                        attacker.setShipsSunk(attacker.getShipsSunk() + 1);
                        System.out.println(attacker.getName() + " has sunk " + defender.getName() + "'s Cruiser!");
                    }
                case 'D':
                    System.out.println("Hit! Destroyer!");
                    defender.des.setTimesHit(defender.des.getTimesHit() + 1);
                    if (defender.des.getTimesHit() == 2) {
                        attacker.setShipsSunk(attacker.getShipsSunk() + 1);
                        System.out.println(attacker.getName() + " has sunk " + defender.getName() + "'s Destroyer!");
                    }
                case 'S':
                    System.out.println("Hit! Submarine!");
                    defender.sub.setTimesHit(defender.sub.getTimesHit() + 1);
                    if (defender.sub.getTimesHit() == 3) {
                        attacker.setShipsSunk(attacker.getShipsSunk() + 1);
                        System.out.println(attacker.getName() + " has sunk " + defender.getName() + "'s Submarine!");
                    }
            }
            // check if win
            if (attacker.getShipsSunk() == 5) {
                System.out.println(attacker.getName() + " has sunk all of " + defender.getName() + "'s ships!");
                System.out.println("GAME OVER");
                gameOver = true;
                System.exit(0);
            }
        } else {
            attacker.getAttackBoard()[row][col] = 'O';
            System.out.println("MISS");
        }
    }
}