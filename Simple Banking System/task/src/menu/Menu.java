package menu;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    MenuContext context;
    List<MenuItem> items;
    List<MenuItem> exitItems;

    public Menu(MenuContext context) {
        super();
        this.context = context;
        items = new ArrayList<>();
        exitItems = new ArrayList<>();
    }

    public void addExitItem(MenuItem exitItem) {
        exitItems.add(exitItem);
    }

    public void addItem(MenuItem menuItem) {
        items.add(menuItem);
    }

    public void render() {
        execute();
    }

    private void execute() {
        final String ERROR_MSG = "Wrong input.";

        while (true) {
            context.getOutputWriter().printMenu(items, exitItems);

            final String option = context.getInputReader().read().trim();

            if (option.isEmpty()) {
                context.getOutputWriter().printError(ERROR_MSG);
                continue;
            }

            MenuItem exitItem = getExitableItemByOption(option);
            if (exitItem != null) {
                exitItem.execute();
                break;
            }

            MenuItem menuItem = getItemByOption(option);
            if (menuItem == null) {
                context.getOutputWriter().printError(ERROR_MSG);
                continue;
            }

            try {
                menuItem.execute();
            } catch (IllegalArgumentException e) {
                context.getOutputWriter().printError(ERROR_MSG);
                context.getOutputWriter().printError(e.getMessage());
                break;
            }
        }
    }

    private MenuItem getItemByOption(String option) {
        for (MenuItem item: items) {
            if (item.getOption().equals(option)) {
                return item;
            }
        }
        return null;
    }

    private MenuItem getExitableItemByOption(String option) {
        for (MenuItem item: exitItems) {
            if (option.equals(item.getOption())) {
                return item;
            }
        }
        return null;
    }

}
