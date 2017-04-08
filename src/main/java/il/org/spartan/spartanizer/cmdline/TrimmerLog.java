package il.org.spartan.spartanizer.cmdline;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.io.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** Logging stuff
 * @author Yossi Gil 
 * @since Sep 20, 2016 */
public enum TrimmerLog {
  DUMMY_ENUM_INSTANCE_INTRODUCING_SINGLETON_WITH_STATIC_METHODS;
  private static CSVStatistics output;
  private static int maxVisitations = 30;
  private static int maxTips = 20;
  private static int maxApplications = 10;
  private static boolean logToScreen = true; // default output
  private static boolean logToFile;
  private static String outputDir = "/tmp/trimmerlog-output.CSV";
  private static String fileName;
  static {
    logToScreen = true;
    off();
  }

  public static void off() {
    maxApplications = maxTips = maxVisitations = -1;
  }

  public static void on() {
    maxApplications = maxTips = maxVisitations = 30;
  }

  public static void activateLogToFile() {
    logToFile = true;
  }

  public static void activateLogToScreen() {
    logToScreen = true;
  }

  public static void application(final ASTRewrite r, final Tip t) {
    if (--maxApplications <= 0) {
      if (maxApplications == 0)
        System.out.println("Stopped logging applications");
      t.go(r, null);
      return;
    }
    System.out.println("      Before: " + r);
    t.go(r, null);
    System.out.println("       After: " + r);
  }

  public static int getMaxApplications() {
    return maxApplications;
  }

  public static int getMaxTips() {
    return maxTips;
  }

  public static int getMaxVisitations() {
    return maxVisitations;
  }

  public static void setFileName(final String $) {
    fileName = $;
  }

  public static void setMaxApplications(final int maxApplications) {
    TrimmerLog.maxApplications = maxApplications;
  }

  public static void setMaxTips(final int maxTips) {
    TrimmerLog.maxTips = maxTips;
  }

  public static void setMaxVisitations(final int maxVisitations) {
    TrimmerLog.maxVisitations = maxVisitations;
  }

  public static void setOutputDir(final String $) {
    TrimmerLog.outputDir = $;
  }

  public static <N extends ASTNode> void tip(final Tipper<N> w, final N n) {
    if (--maxTips <= 0) {
      if (maxTips == 0)
        System.out.println("Stopped logging tips");
      return;
    }
    if (logToFile) {
      init();
      output.put("File", fileName);
      output.put("Tipper", English.name(w));
      output.put("Named", w.description());
      output.put("Kind", w.tipperGroup());
      output.put("Described", w.description(n));
      output.put("Can tip", w.check(n));
      output.put("Suggests", w.tip(n));
      output.nl();
    }
    if (!logToScreen)
      return;
    System.out.println("        File: " + fileName);
    System.out.println("      Tipper: " + English.name(w));
    System.out.println("       Named: " + w.description());
    System.out.println("        Kind: " + w.tipperGroup());
    System.out.println("   Described: " + w.description(n));
    System.out.println("     Can tip: " + w.check(n));
    System.out.println("    Suggests: " + w.tip(n));
  }

  public static void visitation(final ASTNode ¢) {
    if (--maxVisitations > 0)
      System.out.println("VISIT: '" + tide.clean(¢ + "") + "' [" + ¢.getLength() + "] (" + English.name(¢) + ") parent = " + English.name(parent(¢)));
    else if (maxVisitations == 0)
      System.out.println("Stopped logging visitations");
  }

  private static CSVStatistics init() {
    try {
      return output = new CSVStatistics(outputDir, "Tips");
    } catch (final IOException $) {
      return monitor.infoIOException($);
    }
  }
}
