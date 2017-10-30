/**
 * Created by Gavin on 3/4/2017.
 */
public class Player extends Participant {
    public float currentBalance = 0;
    public float currentBet = 0;
    public float totalAmountSpent = 0;

    public void buyIn(int amount) {
        this.totalAmountSpent += amount;
        this.currentBalance += amount;
    }

    public void placeBet(int amount) {
        this.currentBet = amount;
    }
}