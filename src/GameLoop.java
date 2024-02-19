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
            TimeUnit.SECONDS.sleep(2);
            attack(p2, p1);
            TimeUnit.SECONDS.sleep(2);
        }
    }

    private void placeShip(Player p, Ship ship) {
        int row = 0;
        int col = 0;
        char orientation = 'D';
        boolean valid = false;
        String input;
        while (!valid) { //user either has not yet entered coordinates or entered incorrect coordinates
            while (true) {
                System.out.println("Enter the row number followed by the column number of the top left position" +
                        " of your " + ship.getName() + " separated by a space:");
                if (s.hasNextInt()) {
                    row = s.nextInt();
                    if (row <= 10 && row >= 1) {
                        if (s.hasNextInt()) {
                            col = s.nextInt();
                            if (col <= 10 && col >= 1) {
                                break;
                            } else {
                                System.out.println("Column number must be between 1 and 10.");
                            }
                        } else {
                            System.out.println("Invalid input.  Please enter numbers between 1 and 10 separated by a space.");
                            s.nextLine();
                        }
                    } else {
                        System.out.println("Row number must be between 1 and 10.");
                        s.nextLine();
                    }
                } else {
                    System.out.println("Invalid input.  Please enter numbers between 1 and 10 separated by a space.");
                    s.nextLine();
                }
            }
            s.nextLine();

            while (true) {
                System.out.println("Enter either 'D' or 'R' if you want the ship to face right or down");
                input = s.nextLine().toUpperCase();

                if (input.equals("D")) {
                    orientation = 'D';
                    break;
                }
                else if (input.equals("R")) {
                    orientation = 'R';
                    break;
                }
                else {
                    System.out.println("Invalid input. Please enter either 'D' or 'R' if you want the ship to face right or down");
                }
            }
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

    private void attack(Player attacker, Player defender) throws InterruptedException {
        boolean valid = false;
        int row = 0;
        int col = 0;
        printBoard(attacker.getAttackBoard());
        while (!valid) {
            while (true) {
                System.out.println(attacker.getName() + ", enter the row number followed by the column number" +
                        " of the target you wish to attack");
                if (s.hasNextInt()) {
                    row = s.nextInt() - 1;
                    if (row <= 9 && row >= 0) {
                        if (s.hasNextInt()) {
                            col = s.nextInt() - 1;
                            if (col <= 9 && col >= 0) {
                                break;
                            }
                            else {
                                System.out.println("Column must be between 1 and 10.");
                            }
                        }
                        else {
                            System.out.println("Invalid input. Please enter numbers between 1 and 10 separated by a space.");
                            s.nextLine();
                        }
                    }
                    else {
                        System.out.println("Row must be between 1 and 10.");
                        s.nextLine();
                    }
                }
                else {
                    System.out.println("Invalid input. Please enter numbers between 1 and 10 separated by a space.");
                    s.nextLine();
                }
            }
            if (attacker.getAttackBoard()[row][col] == '_') {
                valid = true;
            } else {
                System.out.println("You already attacked this spot, enter a different spot.");
            }
        }

        if (defender.getShipBoard()[row][col] != '_') { //hit
            attacker.getAttackBoard()[row][col] = 'X'; // update attacker board
            //check which ship is hit and if it is sunk
            switch (defender.getShipBoard()[row][col]) {
                case 'B':
                    System.out.println("Hit! Battleship!");
                    defender.bship.setTimesHit(defender.bship.getTimesHit() + 1);
                    if (defender.bship.getTimesHit() == 5) {
                        attacker.setShipsSunk(attacker.getShipsSunk() + 1);
                        System.out.println(attacker.getName() + " has sunk " + defender.getName() + "'s Battleship!");
                    }
                    break;
                case 'A':
                    System.out.println("Hit! Aircraft Carrier!");
                    defender.ac.setTimesHit(defender.ac.getTimesHit() + 1);
                    if (defender.ac.getTimesHit() == 4) {
                        attacker.setShipsSunk(attacker.getShipsSunk() + 1);
                        System.out.println(attacker.getName() + " has sunk " + defender.getName() + "'s Aircraft Carrier!");
                    }
                    break;
                case 'C':
                    System.out.println("Hit! Cruiser!");
                    defender.cru.setTimesHit(defender.cru.getTimesHit() + 1);
                    if (defender.cru.getTimesHit() == 3) {
                        attacker.setShipsSunk(attacker.getShipsSunk() + 1);
                        System.out.println(attacker.getName() + " has sunk " + defender.getName() + "'s Cruiser!");
                    }
                    break;
                case 'D':
                    System.out.println("Hit! Destroyer!");
                    defender.des.setTimesHit(defender.des.getTimesHit() + 1);
                    if (defender.des.getTimesHit() == 2) {
                        attacker.setShipsSunk(attacker.getShipsSunk() + 1);
                        System.out.println(attacker.getName() + " has sunk " + defender.getName() + "'s Destroyer!");
                    }
                    break;
                case 'S':
                    System.out.println("Hit! Submarine!");
                    defender.sub.setTimesHit(defender.sub.getTimesHit() + 1);
                    if (defender.sub.getTimesHit() == 3) {
                        attacker.setShipsSunk(attacker.getShipsSunk() + 1);
                        System.out.println(attacker.getName() + " has sunk " + defender.getName() + "'s Submarine!");
                    }
                    break;
            }
            // check if win
            if (attacker.getShipsSunk() == 5) {
                System.out.println(attacker.getName() + " has sunk all of " + defender.getName() + "'s ships!");
                System.out.println("GAME OVER - " + attacker.getName() + " WINS!!!");
                gameOver = true;
                TimeUnit.SECONDS.sleep(3);
                System.exit(0);
            }
        } else {
            attacker.getAttackBoard()[row][col] = 'O';
            System.out.println("MISS");
        }
    }
}