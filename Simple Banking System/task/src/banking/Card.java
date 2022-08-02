package banking;

public class Card {
    private final long cardNumber;
    private final int pin;
    private long balance;

    public Card(long cardNumber, int pin, long balance) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = balance;
    }

    public long getCardNumber() {
        return cardNumber;
    }

    public int getPin() {
        return pin;
    }

    public boolean checkPin(short pin) {
        return pin == this.pin;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }
}
