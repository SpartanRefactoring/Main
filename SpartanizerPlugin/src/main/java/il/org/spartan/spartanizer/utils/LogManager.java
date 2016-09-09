package il.org.spartan.spartanizer.utils;

import java.io.*;

public class LogManager {
  
  protected static boolean ACTIVE = false;
  private static String logDir = null; // /home/matteo/SpartanLog
  private static String projName = null;
  private static Writer printWriter = null;
  private static LogWriter logWriter = null;
  
  public static void activateLog(){
    ACTIVE = true;
  }
  
  public static void deActivateLog(){
    ACTIVE = false;
  }
  
  public static boolean isActive(){
    return ACTIVE;
  }
    
  public LogManager(final String dir){
    logDir = dir;
  }
  
  public static void initialize(String dir){
    logDir = dir;
    File outputDir = new File(logDir);
    if(!outputDir.exists())
      outputDir.mkdir();
    initializeWriters();
//    printWriter = new Writer();
  }
  
  private static void initializeWriters() {
    logWriter = new LogWriter(logDir);
  }

  public static String getLogDir(){
    return logDir; 
  }

  public static void setLogDir(String dir){
    logDir = dir;
  }
  
//  public class Writer {
//    protected PrintWriter writer = null;
//    protected String outputPath = null;
//    
//    protected void initializeWriter(String outputFileName) {
//      File outputDir = new File(this.outputPath);
//      if (!outputDir.exists()) {
//        outputDir.mkdir();
//      }
//      try {
//        this.writer = new PrintWriter(new BufferedWriter(
//            new FileWriter(outputFileName)));
//      } catch (IOException e) {
//        e.printStackTrace();
//      }
//    }
//  
//    public void close() {
//      this.writer.close();
//    }
//  }
//  
//  /**
//   * Log spartan suggestions 
//   * 
//   * @author matteo
//   * @since 9/2016
//   */
//  
//  public class LogWriter extends Writer {
//    
//    public LogWriter(){
//      outputPath = logDir;
//    }
//    
//    public LogWriter(String outputPath){
//      this.outputPath = outputPath;
//      initializeWriter();
//    }
//
//    private void initializeWriter() {
//      String outputFileName = this.outputPath + "/suggestions.csv";
//      initializeWriter(outputFileName);
//    }
//    
//    public void printRow(String a, String b, String c){
//      this.writer.println(a + "," + b + "," + c);
//      this.writer.flush();
//    }
//  }

  public static LogWriter getLogWriter() {
    return logWriter;
  }

  public static void closeAllWriters() {
    logWriter.close();
  }
 
}
