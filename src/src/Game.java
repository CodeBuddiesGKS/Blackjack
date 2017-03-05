/**
 * Created by Gavin on 3/4/2017.
 */
import java.util.*;

public class Game {
    public static Scanner scanner = new Scanner(System.in);
    public Dealer dealer = new Dealer();
    public Player player = new Player();
    public ArrayList<Card> deck = new ArrayList<>();
    public final int MIN_BET = 5;

    public void start() {
        for (Card.Rank rank : Card.Rank.values()) {
            for (Card.Suit suit : Card.Suit.values()) {
                deck.add(new Card(rank, suit));
            }
        }
        System.out.println("Game starting...");

        dealer.name = "Fucknut";

        System.out.println("Welcome to the Blackjack table. My name is "
                + dealer.name
                + ". I'll be your dealer tonight. "
                + "What is your name?");
        String name = scanner.nextLine();
        player.name = name;
        System.out.println("Hello, " + name + ". Please take a seat. The minimum bet is $"
                + MIN_BET + ".");
    }
}
