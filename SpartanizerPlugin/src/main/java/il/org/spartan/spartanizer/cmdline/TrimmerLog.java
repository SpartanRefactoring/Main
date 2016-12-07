package il.org.spartan.spartanizer.cmdline;

import java.io.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** Logging stuff
 * @author Yossi Gil
 * @year 2016 */
public class TrimmerLog {
  private static CSVStatistics output;
  private static int maxVisitations = 30;
  private static int maxTips = 20;
  private static int maxApplications = 10;
  private static boolean logToScreen = true; // default output
  private static boolean logToFile;
  private static String outputDir = "/tmp/trimmerlog-output.CSV";
  private static String fileName;

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
      output.put("Tipper", clazz(w));
      output.put("Named", w.description());
      output.put("Kind", w.tipperGroup());
      output.put("Described", w.description(n));
      output.put("Can tip", w.canTip(n));
      output.put("Suggests", w.tip(n));
      output.nl();
    }
    if (!logToScreen || !logToScreen)
      return;
    System.out.println("        File: " + fileName);
    System.out.println("       Tipper: " + clazz(w));
    System.out.println("       Named: " + w.description());
    System.out.println("        Kind: " + w.tipperGroup());
    System.out.println("   Described: " + w.description(n));
    System.out.println(" Can tip: " + w.canTip(n));
    System.out.println("    Suggests: " + w.tip(n));
  }

  public static void visitation(final ASTNode ¢) {
    if (--maxVisitations > 0)
      System.out.println("VISIT: '" + tide.clean(¢ + "") + "' [" + ¢.getLength() + "] (" + clazz(¢) + ")" + " parent = " + clazz(parent(¢)));
    else if (maxVisitations == 0)
      System.out.println("Stopped logging visitations");
  }

  private static String clazz(final Object n) {
    return n.getClass().getSimpleName();
  }

  private static CSVStatistics init() {
    try {
      output = new CSVStatistics(outputDir, "Tips");
    } catch (final IOException ¢) {
      ¢.printStackTrace();
    }
    return null;
  }
}
