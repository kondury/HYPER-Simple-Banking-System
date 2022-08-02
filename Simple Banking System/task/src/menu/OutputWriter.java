package menu;

import java.io.Closeable;
import java.util.List;

public interface OutputWriter extends Closeable {

    void printMenu(List<MenuItem> items, List<MenuItem> exitItems);

    void printError(String msg);

    void printMessage(String msg);
}
