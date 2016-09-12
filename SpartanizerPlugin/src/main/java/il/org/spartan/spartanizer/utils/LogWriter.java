package il.org.spartan.spartanizer.utils;

public class LogWriter extends Writer {
  public LogWriter(final String outputPath) {
    this.outputPath = outputPath;
    initializeWriter();
  }

  private void initializeWriter() {
    final String outputFileName = outputPath + "/suggestions.csv";
    initializeWriter(outputFileName);
  }

  public void printRow(final String a, final String b, final String c) {
    writer.println(a + "," + b + "," + c);
    writer.flush();
  }
}
