package il.org.spartan.spartanizer.utils;

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
    initializeWriter((outputPath + "/suggestions.csv"));
  }
}
