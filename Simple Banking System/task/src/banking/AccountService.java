package banking;

import banking.dbservice.DBException;
import banking.dbservice.DBService;

import java.util.Random;

public class AccountService {

    private static final long MIN_CARD_NUMBER_16 = 4000_0000_0000_0000L;
    private static final int PIN_UPPER_BOUND = 10000;
    DBService dbService;
    private Card activeAccount = null;

    public AccountService(String dbFileName) {
        dbService = new DBService(dbFileName);
    }

    public Card createCard() {
        try {
            Card  card = new Card(getNewCardNumber(), getNewPin(), 0L);
            dbService.addCard(card);
            return card;
        } catch (DBException e) {
            return null;
        }
    }

    public boolean isCardExists(long cardNumber) {
        try {
            Card card = dbService.getCard(cardNumber);
            return card != null;
        } catch (DBException e) {
            throw new RuntimeException(e);
        }
    }



    private static int getNewPin() {
        Random random = new Random();
        return random.nextInt(PIN_UPPER_BOUND);
    }

    private long getNewCardNumber() {
        try {
            long lastCardNumber = dbService.getLastCardNumberOrDefault(MIN_CARD_NUMBER_16);
            long newCardFirst15 = lastCardNumber / 10 + 1;
            long sum = getLuhnControlSum(newCardFirst15);
            long checkDigit = (sum * 9) % 10;
            return newCardFirst15 * 10 + checkDigit;
        } catch (DBException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean checkLuhn(long number) {
        long controlDigit = number % 10;
        long controlSum = getLuhnControlSum(number / 10);
        return (controlSum + controlDigit) % 10 == 0;
    }

    private static long getLuhnControlSum(long number) {
        int multiplier = 2;
        long sum = 0;
        while (number != 0) {
            long additive = (number % 10) * multiplier;
            if (additive > 9) {
                additive -= 9;
            }
            sum += additive;
            multiplier = 1 + multiplier % 2;
            number /= 10;
        }
        return sum;
    }

    public Card getActiveAccount() {
        return activeAccount;
    }

    public Card getAccount(long cardNumber) {
        try {
            return dbService.getCard(cardNumber);
        } catch (DBException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean login(long cardNumber, int pin) {
        Card card;
        try {
            card = dbService.getCard(cardNumber);
        } catch (DBException e) {
            throw new RuntimeException(e);
        }

        if (card != null && card.getPin() == pin) {
            activeAccount = card;
            return true;
        }
        return false;
    }

    public void logout() {
        activeAccount = null;
    }

    public void close() {
        logout();
        try {
            dbService.close();
        } catch (DBException e) {
            throw new RuntimeException(e);
        }
    }

    public void addIncome(Card account, long income) {
        try {
            dbService.addIncome(account, income);
        } catch (DBException e) {
            throw new RuntimeException(e);
        }
    }

    public void addIncome(long income) {
        try {
            dbService.addIncome(getActiveAccount(), income);
            activeAccount.setBalance(activeAccount.getBalance() + income);
        } catch (DBException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeAccount() {
        try {
            dbService.removeAccount(getActiveAccount());
        } catch (DBException e) {
            throw new RuntimeException(e);
        }
    }

    public void transfer(Card toAccount, long sum) {
        if (getActiveAccount() == null) {
            return;
        }

        try {
            dbService.transfer(getActiveAccount(), toAccount, sum);
            activeAccount.setBalance(activeAccount.getBalance() - sum);
        } catch (DBException e) {
            throw new RuntimeException(e);
        }
    }
}
