import java.util.*;

/**
 * Created by Gavin on 3/4/2017.
 */
public class Participant {
    public static Random rng = new Random();
    public String name;
    public ArrayList<Card> hand = new ArrayList<>();

    public int getSumOfParticipantsHand() {
        int sum = calcSum();
        boolean largeAceFound = true;

        while (sum > 21 && largeAceFound) {
            largeAceFound = false;
            for (Card c : this.hand) {
                if (c.rank == Card.Rank.Ace && c.value == 11) {
                    c.value = 1;
                    largeAceFound = true;
                    break;
                }
            }
            sum = calcSum();
        }
        return sum;
    }

    private int calcSum() {
        int sum = 0;
        for (Card c : this.hand) {
            sum += c.value;
        }
        return sum;
    }

    public void drawCard(ArrayList<Card> deck) {
        this.hand.add(deck.remove(rng.nextInt(deck.size())));
    }

    public void drawCard(ArrayList<Card> deck, boolean hidden) {
        Card c = deck.remove(rng.nextInt(deck.size()));
        c.hidden = hidden;
        this.hand.add(c);
    }
}