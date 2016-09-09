package il.org.spartan.spartanizer.utils;

public class LogWriter extends Writer {
  
  public LogWriter(String outputPath){
    this.outputPath = outputPath;
    initializeWriter();
  }

  private void initializeWriter() {
    String outputFileName = this.outputPath + "/suggestions.csv";
    initializeWriter(outputFileName);
  }
  
  public void printRow(String a, String b, String c){
    this.writer.println(a + "," + b + "," + c);
    this.writer.flush();
  }
}
