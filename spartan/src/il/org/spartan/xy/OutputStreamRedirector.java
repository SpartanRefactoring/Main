package il.org.spartan.xy;

import java.io.*;

import org.jetbrains.annotations.*;

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
      for (final int nextChar = from.read(); nextChar != -1;)
        to.append((char) nextChar);
    } catch (@NotNull final IOException ¢) {
      ¢.printStackTrace();
    }
  }
}