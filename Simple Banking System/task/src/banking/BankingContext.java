package banking;

import menu.Menu;
import menu.MenuContext;
import menu.MenuItem;

public class BankingContext extends MenuContext {
    public static final String CREATE_ACCOUNT_ITEM_STR = "1. Create an account";
    public static final String LOGIN_ITEM_STR = "2. Log into account";
    public static final String EXIT_ITEM_STR = "0. Exit";
    public static final String BALANCE_ITEM_STR = "1. Balance";
    private static final String ADD_INCOME_ITEM_STR = "2. Add income";
    private static final String DO_TRANSFER_ITEM_STR = "3. Do transfer";
    private static final String CLOSE_ACCOUNT_ITEM_STR = "4. Close account";
    public static final String LOGOUT_ITEM_STR = "5. Log out";

    public static final String CARD_CREATED_STR = "\nYour card has been created\n" +
            "Your card number:\n" +
            "%s\n" +
            "Your card PIN:\n" +
            "%04d\n";
    public static final String WRONG_CARD_OR_PIN_STR = "\nWrong  card number or PIN!\n";
    public static final String LOGGED_IN_STR = "\nYou have successfully logged in!\n";
    public static final String LOGGED_OUT_STR = "\nYou have successfully logged out!\n";
    public static final String BALANCE_TMPL_STR = "\nBalance: %s\n";
    public static final String ENTER_CARD_STR = "\nEnter your card number:";
    public static final String ENTER_PIN_STR = "Enter your PIN:";
    public static final String EXITED_STR = "\nBye!";
    private static final String ENTER_INCOME_STR = "\nEnter income:";
    private static final String INCOME_ADDED_STR = "Income was added!\n";
    private static final String ACCOUNT_CLOSED_STR = "\nThe account has been closed!\n";
    private static final String TRANSFER_STR = "\nTransfer";
    private static final String TRANSFER_ENTER_CARD_STR = "Enter card number:";
    private static final String INVALID_CARD_NUMBER_STR =
            "Probably you made a mistake in the card number. Please try again!\n";
    private static final String CARD_DOES_NOT_EXIST_STR = "Such a card does not exist.\n";
    private static final String ENTER_SUM_TO_TRANSFER_STR = "Enter how much money you want to transfer:";
    private static final String NOT_ENOUGH_MONEY_STR = "Not enough money!\n";
    private static final String SAME_CARD_STR = "You can't transfer money to the same account!";
    private static final String TRANSFER_SUCCESS_STR = "Success!\n";

    private static BankingContext context;
    private AccountService accountService;

    private BankingContext() {
        super(new CLIReader(), new CLIWriter());
    }

    public static BankingContext getInstance() {
        if (context == null) {
            context = new BankingContext();
        }
        return context;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void createCard() {
        Card card = accountService.createCard();
        String msg = String.format(CARD_CREATED_STR, card.getCardNumber(), card.getPin());
        getOutputWriter().printMessage(msg);
    }

    public void login() {
        getOutputWriter().printMessage(ENTER_CARD_STR);
        long cardNumber = Long.parseLong(getInputReader().read());
        getOutputWriter().printMessage(ENTER_PIN_STR);
        int pin = Integer.parseInt(getInputReader().read());
        if (accountService.login(cardNumber, pin)) {
            getOutputWriter().printMessage(LOGGED_IN_STR);
            Menu accountMenu = buildAccountMenu();
            accountMenu.render();
        } else {
            getOutputWriter().printError(WRONG_CARD_OR_PIN_STR);
        }
    }

    public void exit() {
        accountService.close();
        getOutputWriter().printMessage(EXITED_STR);
        context.close();
        System.exit(0);
    }

    public void balance() {
        String msg = String.format(BALANCE_TMPL_STR, accountService.getActiveAccount().getBalance());
        getOutputWriter().printMessage(msg);
    }

    private void addIncome() {
        getOutputWriter().printMessage(ENTER_INCOME_STR);
        long income = Long.parseLong(getInputReader().read());
        accountService.addIncome(income);
        getOutputWriter().printMessage(INCOME_ADDED_STR);
    }

    private void doTransfer() {
        getOutputWriter().printMessage(TRANSFER_STR);

        getOutputWriter().printMessage(TRANSFER_ENTER_CARD_STR);
        long destCardNumber = Long.parseLong(getInputReader().read());

        if (accountService.getActiveAccount().getCardNumber() == destCardNumber) {
            getOutputWriter().printError(SAME_CARD_STR);
            return;
        }

        if (!AccountService.checkLuhn(destCardNumber)) {
            getOutputWriter().printError(INVALID_CARD_NUMBER_STR);
            return;
        }

        if (!accountService.isCardExists(destCardNumber)) {
            getOutputWriter().printError(CARD_DOES_NOT_EXIST_STR);
            return;
        }

        getOutputWriter().printMessage(ENTER_SUM_TO_TRANSFER_STR);
        long sum = Long.parseLong(getInputReader().read());

        if (sum > accountService.getActiveAccount().getBalance()) {
            getOutputWriter().printError(NOT_ENOUGH_MONEY_STR);
            return;
        }

        accountService.transfer(accountService.getAccount(destCardNumber), sum);

        getOutputWriter().printMessage(TRANSFER_SUCCESS_STR);


    }
    private void closeAccount() {
        accountService.closeAccount();
        getOutputWriter().printMessage(ACCOUNT_CLOSED_STR);
    }

    public void logout() {
        accountService.logout();
        getOutputWriter().printMessage(LOGGED_OUT_STR);
    }


    public Menu buildMainMenu() {
        Menu menu = new Menu(this);
        menu.addItem(new MenuItem("1", CREATE_ACCOUNT_ITEM_STR, this::createCard));
        menu.addItem(new MenuItem("2", LOGIN_ITEM_STR, this::login));
        menu.addExitItem(new MenuItem("0", EXIT_ITEM_STR, this::exit));
        return menu;
    }

    public Menu buildAccountMenu() {
        Menu menu = new Menu(this);
        menu.addItem(new MenuItem("1", BALANCE_ITEM_STR, this::balance));
        menu.addItem(new MenuItem("2", ADD_INCOME_ITEM_STR, this::addIncome));
        menu.addItem(new MenuItem("3", DO_TRANSFER_ITEM_STR, this::doTransfer));
        menu.addExitItem(new MenuItem("4", CLOSE_ACCOUNT_ITEM_STR, this::closeAccount));
        menu.addExitItem(new MenuItem("5", LOGOUT_ITEM_STR, this::logout));
        menu.addExitItem(new MenuItem("0", EXIT_ITEM_STR, this::exit));
        return menu;
    }

}
