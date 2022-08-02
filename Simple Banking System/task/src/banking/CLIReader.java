package banking;

import menu.InputReader;

import java.io.IOException;
import java.util.Scanner;

public class CLIReader implements InputReader {

    private final Scanner scanner = new Scanner(System.in);

    public String read() {
        return scanner.nextLine();
    }

    @Override
    public void close() throws IOException {
        scanner.close();
    }
}
