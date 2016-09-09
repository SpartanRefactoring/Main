package il.org.spartan.spartanizer.utils;

import java.io.*;

public class Writer {
  protected PrintWriter writer = null;
  protected String outputPath = null;
  
  protected void initializeWriter(String outputFileName) {
    File outputDir = new File(this.outputPath);
    if (!outputDir.exists()) {
      outputDir.mkdir();
    }
    try {
      this.writer = new PrintWriter(new BufferedWriter(
          new FileWriter(outputFileName)));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void close() {
    this.writer.close();
  }
}