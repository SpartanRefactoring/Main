package il.org.spartan.spartanizer.utils;

import java.io.*;

// TODO: Matteo Remove this one --yg
class Writer {
  protected PrintWriter writer;
  protected String outputPath;

  public void close() {
    writer.close();
  }
  protected void initializeWriter(final String outputFileName) {
    // TODO yossigil , call to this function with Writer and not with one of his
    // descendant‏ cause nullPointerException maybe need to be abstract?
    final File outputDir = new File(outputPath);
    if (!outputDir.exists())
      outputDir.mkdir();
    try {
      writer = new PrintWriter(new BufferedWriter(new FileWriter(outputFileName)));
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }
}
// TODO Matteo: erase this class
public final class LogWriter extends Writer {
  public LogWriter(final String outputPath) {
    this.outputPath = outputPath;
    initializeWriter();
  }
  public void printRow(final String a, final String b, final String c) {
    writer.println(a + "," + b + "," + c);
    writer.flush();
  }
  private void initializeWriter() {
    initializeWriter(outputPath + "/tips.csv");
  }
}
