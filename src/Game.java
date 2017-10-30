/**
 * Created by Gavin on 3/4/2017.
 */
import java.text.DecimalFormat;
import java.util.*;

public class Game {
    public static Scanner scanner = new Scanner(System.in);
    public Dealer dealer = new Dealer();
    public Player player = new Player();
    public ArrayList<Card> deck = new ArrayList<>();
    public final int MIN_BET = 5;
    public final int NUMBER_OF_DECKS = 6;
    private boolean isRoundFinished;
    private DecimalFormat currency = new DecimalFormat();

    Game() {
        currency.setMinimumFractionDigits(2);
        currency.setMaximumFractionDigits(2);
    }

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
        System.out.print("Hello, " + name + ". Please take a seat. ");
    }

    private void promptBuyIn() {
        System.out.println("The minimum bet is $" + currency.format(MIN_BET) + ".");
        System.out.println("How much would you like to buy in for?");
        System.out.print("$");
        int buyInAmount = scanner.nextInt();
        while(buyInAmount < MIN_BET || buyInAmount % 5 != 0) {
            if (buyInAmount < MIN_BET) {
                System.out.println("You must buy in for at least $" + currency.format(MIN_BET) + ".");
            } else if (buyInAmount % 5 != 0) {
                System.out.println("You can only buy in for increments of " + currency.format(MIN_BET) + ".");
            }
            System.out.print("$");
            buyInAmount = scanner.nextInt();
        }
        player.buyIn(buyInAmount);
    }

    private void promptBet() {
        System.out.println("Current Balance: $" + currency.format(player.currentBalance));
        System.out.println("Please place your bet:");
        System.out.print("$");
        int betAmount = scanner.nextInt();
        while(betAmount > player.currentBalance || betAmount % 5 != 0) {
            if(betAmount > player.currentBalance) {
                System.out.println("You cannot bet more than your Current Balance of $" + currency.format(player.currentBalance) + ".");
            } else if(betAmount % 5 != 0) {
                System.out.println("You can only bet in increments of " + currency.format(MIN_BET) + ".");
            }
            System.out.print("$");
            betAmount = scanner.nextInt();
        }
        player.placeBet(betAmount);
    }

    private void gameLoop() {
        while (player.currentBalance > 0) {
            deal();
            displayHands();

            isRoundFinished = false;
            if (dealer.getSumOfParticipantsHand() == 21) {
                System.out.println("Dealer has 21, you lose!");
                collectBetAfterLoss();
                isRoundFinished = true;
            }
            if (!isRoundFinished) {
                playerPhase();
            }
            if (!isRoundFinished) {
                dealerPhase();
            }
            if (!isRoundFinished) {
                payoutPhase();
            }

            zeroBalanceCheck();
            resetHands();
            checkDeckSize();
        }
    }

    private void deal() {
        System.out.println("Dealing cards...");
        player.drawCard(deck);
        dealer.drawCard(deck, true);
        player.drawCard(deck);
        dealer.drawCard(deck);
    }

    private void displayHands() {
        System.out.print("Dealers Hand: ");
        for(Card c : dealer.hand) {
            if (c.hidden) {
                System.out.print("*** ");
            } else {
                System.out.print(c.rank + " ");
            }
        }
        System.out.print("\nYour Hand: ");
        for(Card c : player.hand) {
            System.out.print(c.rank + " ");
        }
        System.out.println();
    }

    private void playerPhase() {
        if (player.hand.get(0).value + player.hand.get(1).value == 21) {
            System.out.println("Blackjack, you win!");
            player.currentBalance += player.currentBet * 1.5;
            isRoundFinished = true;
        } else {
            System.out.println("Hit or Stay? (h/s)");
            String hitOrStay = scanner.next().toLowerCase();
            while (hitOrStay.equals("h") && !isRoundFinished) {
                player.drawCard(deck);
                displayHands();
                if (player.getSumOfParticipantsHand() > 21) {
                    System.out.println("You broke, you lose!");
                    collectBetAfterLoss();
                    isRoundFinished = true;
                } else {
                    System.out.println("Hit or Stay? (h/s)");
                    hitOrStay = scanner.next().toLowerCase();
                }
            }
        }
    }

    private void dealerPhase() {
        dealer.hand.get(0).hidden = false;
        displayHands();
        while (dealer.getSumOfParticipantsHand() < 17) {
            dealer.drawCard(deck);
            displayHands();
        }
    }

    private void payoutPhase() {
        int playerVal = player.getSumOfParticipantsHand();
        int dealerVal = dealer.getSumOfParticipantsHand();

        if (dealerVal > 21) {
            System.out.println("Dealer broke, you win!");
            payPlayer();
        } else if (playerVal > dealerVal) {
            System.out.println(playerVal + " beats " + dealerVal + ", you win!");
            payPlayer();
        } else {
            System.out.println(dealerVal + " beats " + playerVal + ", you lose!");
            collectBetAfterLoss();
        }
    }

    private void payPlayer() {
        int playerVal = player.getSumOfParticipantsHand();
        if (playerVal == 21) {
            player.currentBalance += player.currentBet * 1.5;
        } else {
            player.currentBalance += player.currentBet;
        }
    }

    private void collectBetAfterLoss() {
        player.currentBalance -= player.currentBet;
    }

    private void zeroBalanceCheck() {
        if (player.currentBalance <= 0) {
            System.out.println("You seem to have run out of money. Would you like to buy back in? (y/n)");
            String outOfMoneyResponse = scanner.next().toLowerCase();
            if(outOfMoneyResponse.equals("y")) {
                promptBuyIn();
                promptBet();
            }
        } else {
            promptCashOut();
        }
    }

    private void promptCashOut() {
        System.out.println("Current Balance: $" + currency.format(player.currentBalance));
        System.out.println("Would you like to cash out? (y/n)");
        String cashOutResponse = scanner.next().toLowerCase();
        if(cashOutResponse.equals("y")) {
            System.out.println("You spent $" + currency.format(player.totalAmountSpent) + " and left with $" + currency.format(player.currentBalance) + ".");
            float diff = player.currentBalance - player.totalAmountSpent;
            if (diff > 0) {
                System.out.println("Congratulations, you won $" + currency.format(diff) + "!");
            } else {
                System.out.println("Sorry, you lost $" + currency.format(Math.abs(diff)) + ".");
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