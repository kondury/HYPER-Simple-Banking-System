package banking;

import menu.MenuItem;
import menu.OutputWriter;

import java.io.IOException;
import java.util.List;

public class CLIWriter implements OutputWriter {
    @Override
    public void printMenu(List<MenuItem> items, List<MenuItem> exitItems) {
        for (MenuItem item: items) {
            System.out.println(item.getName());
        }
        for (MenuItem item: exitItems) {
            System.out.println(item.getName());
        }
    }

    @Override
    public void printError(String msg) {
        System.out.println(msg);
    }

    @Override
    public void printMessage(String msg) {
        System.out.println(msg);
    }

    @Override
    public void close() throws IOException {

    }
}
