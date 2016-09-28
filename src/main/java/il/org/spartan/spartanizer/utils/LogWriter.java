package il.org.spartan.spartanizer.utils;

public final class LogWriter extends Writer {
  public LogWriter(final String outputPath) {
    this.outputPath = outputPath;
    initializeWriter();
  }

  private void initializeWriter() {
    initializeWriter(outputPath + "/tips.csv");
  }

  public void printRow(final String a, final String b, final String c) {
    writer.println(a + "," + b + "," + c);
    writer.flush();
  }
}
