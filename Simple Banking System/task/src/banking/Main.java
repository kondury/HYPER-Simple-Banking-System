package banking;

import menu.Menu;

public class Main {

    public static void main(String[] args) {
        String fileName = getParameter(args, "-fileName");

        BankingContext bankingContext = BankingContext.getInstance();
        AccountService accountService = new AccountService(fileName);
        bankingContext.setAccountService(accountService);
        Menu menu = bankingContext.buildMainMenu();
        menu.render();
    }

    private static String getParameter(String[] args, String parameterName) {
        boolean isParameterFound = false;
        for (String arg : args) {
            if (isParameterFound) {
                return arg;
            }
            isParameterFound = arg.equals(parameterName);
        }
        return null;
    }
}