package menu;

import java.io.Closeable;

public interface InputReader extends Closeable {

    String read();

}