import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        Scanner s = new Scanner(System.in);
        System.out.println("Enter Player 1 name");
        Player p1 = new Player(s.next());
        System.out.println("Enter Player 2 name");
        Player p2 = new Player(s.next());
        GameLoop g = new GameLoop();
        g.gameLoop(p1, p2);
    }

}