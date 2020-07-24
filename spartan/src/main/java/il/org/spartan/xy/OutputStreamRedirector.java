package il.org.spartan.xy;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

class OutputStreamRedirector extends Thread {
  private final InputStream from;
  private final PrintStream to;

  OutputStreamRedirector(final PrintStream to, final InputStream from) {
    this.from = from;
    this.to = to;
    start();
  }

  @Override public void run() {
    try {
      for (int nextChar = from.read(); nextChar != -1; nextChar = from.read())
        to.append((char) nextChar);
    } catch (final IOException ¢) {
      ¢.printStackTrace();
    }
  }
}