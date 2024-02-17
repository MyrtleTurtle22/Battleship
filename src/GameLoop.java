import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class GameLoop {
    Scanner s = new Scanner(System.in);

    // Create ships for each player
    Ship p1bship = new Ship(5, "Battleship");
    Ship p1ac = new Ship(4, "Aircraft Carrier");
    Ship p1sub = new Ship(3, "Submarine");
    Ship p1cru = new Ship(3, "Cruiser");
    Ship p1des = new Ship(2, "Destroyer");

    Ship p2bship = new Ship(5, "Battleship");
    Ship p2ac = new Ship(4, "Aircraft Carrier");
    Ship p2sub = new Ship(3, "Submarine");
    Ship p2cru = new Ship(3, "Cruiser");
    Ship p2des = new Ship(2, "Destroyer");


    public void gameLoop(Player p1, Player p2) throws InterruptedException, IOException {
        System.out.println(p1.getName() + " will place their ships first.");
        printBoard(p1.getShipBoard());
        placeShip(p1, p1bship);
        printBoard(p1.getShipBoard());
        placeShip(p1, p1ac);
        printBoard(p1.getShipBoard());
        placeShip(p1, p1cru);
        printBoard(p1.getShipBoard());
        placeShip(p1, p1sub);
        printBoard(p1.getShipBoard());
        placeShip(p1, p1des);
        printBoard(p1.getShipBoard());
        System.out.println(p1.getName() + ", you have 10 seconds to" +
                " look at your board before " + p2.getName() + " places their ships.");
        TimeUnit.SECONDS.sleep(10);
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

        System.out.println("Now " + p2.getName() + " places their ships.");
        printBoard(p2.getShipBoard());
        placeShip(p2, p2bship);
        printBoard(p2.getShipBoard());
        placeShip(p2, p2ac);
        printBoard(p2.getShipBoard());
        placeShip(p2, p2cru);
        printBoard(p2.getShipBoard());
        placeShip(p2, p2sub);
        printBoard(p2.getShipBoard());
        placeShip(p2, p2des);
        printBoard(p2.getShipBoard());
    }

    private void placeShip(Player p, Ship ship) {
        int x = 0;
        int y = 0;
        char orientation = 'D';
        boolean valid = false;
        while (!valid) { //user either has not yet entered coordinates or entered incorrect coordinates
            System.out.println("Enter the row number followed by the column number of the top left position" +
                    " of your " + ship.getName() + " separated by a space:");
            x = s.nextInt();
            y = s.nextInt();
            s.nextLine();

            System.out.println("Enter either 'D' or 'R' if you want the ship to face right or down");
            orientation = s.next().charAt(0);
            valid = validateCoordinates(p, ship, x, y, orientation);
        }
        //change each ship position to 'X'
        modifyShipBoard(p, ship, x, y, orientation);
    }

    private boolean validateCoordinates(Player p, Ship s, int x, int y, char orientation) {
        if (orientation == 'R') {
            if (y > (11 - s.getLength())) {
                System.out.println("Invalid position, end of ship would be off the board");
                return false;
            }
            for (int i = 0; i < s.getLength(); i++) {
                if (p.getShipBoard()[x-1][y+i-1] == 'X') {
                    System.out.println("Spot already taken");
                    return false;
                }
            }
        }  else if (orientation == 'D') {
            if (x > (11 - s.getLength())) {
                System.out.println("Invalid position, bottom of ship would be off the board");
                return false;
            }
            for (int i = 0; i < s.getLength(); i++) {
                if (p.getShipBoard()[x+i - 1][y - 1] == 'X') {
                    System.out.println("Spot already taken");
                    return false;
                }
            }
        }

        return true;
    }

    private void modifyShipBoard(Player p, Ship s, int x, int y, char orientation) {
        //arrays are zero indexed but players will use 1 indexing
        if (orientation == 'R') {
            for (int i = 0; i < s.getLength(); i++) {
                p.getShipBoard()[x-1][y+i-1] = 'X';
            }
        }
        else {
            for (int i = 0; i < s.getLength(); i++) {
                p.getShipBoard()[x+i-1][y-1] = 'X';
            }
        }
    }

    private void printBoard(char[][] board) {
        System.out.printf("%-4s", "");
        for (int i = 0; i < board[0].length; i++) {
            System.out.printf("%-4d", i+1);
        }
        System.out.println();
        for (int i = 0; i < board.length; i++) {
            System.out.printf("%-4d", i+1);
            for (int j = 0; j < board[0].length; j++) {
                System.out.printf("%-4c", board[i][j]);
            }
            System.out.println();
        }

    }

    // select attack spot

    // evaluate shot

    // ship sunk

    // end game
}
