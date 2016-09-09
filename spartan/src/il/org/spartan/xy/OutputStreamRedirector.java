/**
 *
 */
package il.org.spartan.xy;

import java.io.*;

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
      int nextChar;
      while ((nextChar = from.read()) != -1)
        to.append((char) nextChar);
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }
}