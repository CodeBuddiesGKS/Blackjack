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
    public final int NUMBER_OF_DECKS = 6;

    public void start() {
        System.out.println("Initializing deck...");
        initializeDeck();

        System.out.println("Game starting...");

        promptName();
        promptBuyIn();
        promptBet();

        //Start game loop
        gameLoop();

        System.out.println("Thanks for playing!");
    }

    private void initializeDeck() {
        this.deck = new ArrayList<>();
        for(int i = 0; i < this.NUMBER_OF_DECKS; i++) {
            for (Card.Rank rank : Card.Rank.values()) {
                for (Card.Suit suit : Card.Suit.values()) {
                    this.deck.add(new Card(rank, suit));
                }
            }
        }
    }

    private void promptName() {
        System.out.println("Welcome to the Blackjack table. My name is "
                + dealer.name
                + ". I'll be your dealer tonight.");
        System.out.println("What is your name?");
        String name = scanner.nextLine();
        player.name = name;
        System.out.println("Hello, " + name + ". Please take a seat. The minimum bet is $" + MIN_BET + ".");
    }

    private void promptBuyIn() {
        System.out.println("How much would you like to buy in for?");
        System.out.print("$");
        int buyInAmount = scanner.nextInt();
        while(buyInAmount < MIN_BET || buyInAmount % 5 != 0) {
            if (buyInAmount < MIN_BET) {
                System.out.println("You must buy in for at least $" + MIN_BET + ".");
            } else if (buyInAmount % 5 != 0) {
                System.out.println("You can only buy in for increments of " + MIN_BET + ".");
            }
            System.out.print("$");
            buyInAmount = scanner.nextInt();
        }
        player.buyIn(buyInAmount);
    }

    private void promptBet() {
        System.out.println("Please place your bet:");
        System.out.print("$");
        int betAmount = scanner.nextInt();
        while(betAmount > player.currentBalance || betAmount % 5 != 0) {
            if(betAmount > player.currentBalance) {
                System.out.println("You cannot bet more than your Balance of $" + player.currentBalance + ".");
            } else if(betAmount % 5 != 0) {
                System.out.println("You can only bet in increments of " + MIN_BET + ".");
            }
            System.out.print("$");
            betAmount = scanner.nextInt();
        }
        player.placeBet(betAmount);
    }

    private void gameLoop() {
        while(player.currentBalance > 0) {
            System.out.println("Dealing cards...");

            deal();
            displayHands();

            // Player phase
            boolean broke = false;
            if (player.hand.get(0).value + player.hand.get(1).value == 21) {
                System.out.println("Blackjack, you win!");
                player.currentBalance += player.currentBet * 1.5;
            } else {
                System.out.println("Hit or Stay? (h/s)");
                String hitOrStay = scanner.next().toLowerCase();
                while (hitOrStay.equals("h") && !broke) {
                    player.drawCard(deck);
                    displayHands();
                    if (player.getSumOfParticipantsHand() > 21) {
                        broke = true;
                        System.out.println("You broke, you lose!");
                    } else {
                        System.out.println("Hit or Stay? (h/s)");
                        hitOrStay = scanner.next().toLowerCase();
                    }
                }
            }

            //Dealer phase
            //flipDealersHiddenCard();
            if (!broke && dealer.getSumOfParticipantsHand() < 17) {
                dealer.drawCard(deck);
                displayHands();
            }


            if (player.currentBalance <= 0) {
                System.out.println("You seem to have run out of money. Would you like to buy back in? (y/n)");
                String outOfMoneyResponse = scanner.next().toLowerCase();
                if(outOfMoneyResponse == "y") {
                    promptBuyIn();
                    promptBet();
                }
            } else {
                promptCashOut();
            }

            resetHands();
            checkDeckSize();
        }
    }

    private void deal() {
        player.drawCard(deck);
        dealer.drawCard(deck, true);
        player.drawCard(deck);
        dealer.drawCard(deck);
    }

    private void displayHands() {
        System.out.print("Dealers Hand: ");
        for(Card c : dealer.hand) {
            System.out.print(c.rank + " ");
        }
        System.out.print("\nYour Hand: ");
        for(Card c : player.hand) {
            System.out.print(c.rank + " ");
        }
        System.out.println();
    }

    private void promptCashOut() {
        System.out.println("Would you like to cash out? (y/n)");
        String cashOutResponse = scanner.next();
        if(cashOutResponse.toLowerCase().equals("y")) {
            System.out.println("You spent $" + player.totalAmountSpent + " and left with " + player.currentBalance + ".");
            int diff = player.currentBalance - player.totalAmountSpent;
            if (diff > 0) {
                System.out.println("Congratulations, you won $" + diff + "!");
            } else {
                System.out.println("Sorry, you lost $" + Math.abs(diff) + ".");
            }
            player.currentBalance = 0;
        } else {
            promptBet();
        }
    }

    private void resetHands() {
        dealer.hand = new ArrayList<>();
        player.hand = new ArrayList<>();
    }

    private void checkDeckSize() {
        if(this.deck.size() < 52) {
            System.out.println("Shuffling deck...");
            this.initializeDeck();
        }
    }
}
