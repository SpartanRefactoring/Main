package il.org.spartan.spartanizer.utils;

import java.io.*;

public class Writer {
  protected PrintWriter writer;
  protected String outputPath;

  public void close() {
    writer.close();
  }

  protected void initializeWriter(final String outputFileName) {
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