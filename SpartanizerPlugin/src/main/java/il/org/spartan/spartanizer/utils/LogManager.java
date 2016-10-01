package il.org.spartan.spartanizer.utils;

import java.io.*;

public final class LogManager {
  protected static boolean ACTIVE;
  private static String logDir; // /home/matteo/SpartanLog
  private static LogWriter logWriter;

  public static void activateLog() {
    ACTIVE = true;
  }

  public static void closeAllWriters() {
    logWriter.close();
  }

  public static void deActivateLog() {
    ACTIVE = false;
  }

  public static String getLogDir() {
    return logDir;
  }

  public static LogWriter getLogWriter() {
    return logWriter;
  }

  public static void initialize(final String dir) {
    logDir = dir;
    final File outputDir = new File(logDir);
    if (!outputDir.exists())
      outputDir.mkdir();
    initializeWriters();
    // printWriter = new Writer();
  }

  public static boolean isActive() {
    return ACTIVE;
  }

  public static void setLogDir(final String dir) {
    logDir = dir;
  }

  private static void initializeWriters() {
    logWriter = new LogWriter(logDir);
  }

  public LogManager(final String dir) {
    logDir = dir;
  }
}
