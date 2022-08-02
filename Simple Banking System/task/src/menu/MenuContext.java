package menu;

import java.io.IOException;

public class MenuContext {

    private final InputReader inputReader;
    private final OutputWriter outputWriter;

    public MenuContext(InputReader inputReader, OutputWriter outputWriter) {
        this.inputReader = inputReader;
        this.outputWriter = outputWriter;
    }

    public InputReader getInputReader() {
        return inputReader;
    }

    public OutputWriter getOutputWriter() {
        return outputWriter;
    }

    public void close() {
        try {
            inputReader.close();
        } catch (IOException e) {
            ;
        }
        try {
            outputWriter.close();
        } catch (IOException e) {
            ;
        }
    }
}
