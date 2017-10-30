/**
 * Created by Gavin on 3/4/2017.
 */
public class Card {
    public enum Rank { Ace, Two, Three, Four, Five, Six, Seven, Eight, Nine, Ten, Jack, Queen, King }
    public enum Suit { Heart, Diamond, Club, Spade }
    public final Rank rank;
    public final Suit suit;
    public Integer value;
    public boolean hidden = false;

    public String toString() {
        return "Rank: " + rank + " | " +
                "Suit: " + suit + " | " +
                "Value: " + value;
    }

    Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        switch (rank) {
            case Ace:
                this.value = 11;
                break;
            case Two:
                this.value = 2;
                break;
            case Three:
                this.value = 3;
                break;
            case Four:
                this.value = 4;
                break;
            case Five:
                this.value = 5;
                break;
            case Six:
                this.value = 6;
                break;
            case Seven:
                this.value = 7;
                break;
            case Eight:
                this.value = 8;
                break;
            case Nine:
                this.value = 9;
                break;
            case Ten:
            case Jack:
            case Queen:
            case King:
                this.value = 10;
                break;
        }
    }
}