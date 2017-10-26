/**
 * Created by Gavin on 3/4/2017.
 */
import java.util.*;
public class Player extends Participant {

    public static Scanner scanner = new Scanner(System.in);
    public int currentBalance = 0;
    public int currentBet = 0;
    public int totalAmountSpent = 0;

    public void buyIn(int amount) {
        this.totalAmountSpent += amount;
        this.currentBalance += amount;
    }

    public void placeBet(int amount) {
        this.currentBalance -= amount;
        this.currentBet = amount;
    }
}