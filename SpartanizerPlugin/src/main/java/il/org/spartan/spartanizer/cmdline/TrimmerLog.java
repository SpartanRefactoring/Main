package il.org.spartan.spartanizer.cmdline;

import java.io.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;

import static il.org.spartan.spartanizer.ast.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.wringing.*;

/** Logging stuff
 * @author Yossi Gil
 * @year 2016 */
public class TrimmerLog {
  private static CSVStatistics output;
  private static int maxVisitations = 30;
  private static int maxSuggestions = 20;
  private static int maxApplications = 10;
  private static boolean logToScreen = true; // default output
  private static boolean logToFile;
  private static String outputDir = "/tmp/trimmerlog-output.CSV";

  private static String fileName;

  public static void application(final ASTRewrite r, final Suggestion s) {
    if (--maxApplications <= 0) {
      if (maxApplications == 0)
        System.out.println("Stopped logging applications");
      s.go(r, null);
      return;
    }
    System.out.println("      Before: " + r);
    s.go(r, null);
    System.out.println("       After: " + r);
  }

  public static void setMaxSuggestions(final int maxSuggestions) {
    TrimmerLog.maxSuggestions = maxSuggestions;
  }

  public static void setMaxVisitations(final int maxVisitations) {
    TrimmerLog.maxVisitations = maxVisitations;
  }

  public static void setOutputDir(final String $) {
    TrimmerLog.outputDir = $;
  }

  public static <N extends ASTNode> void suggestion(final Wring<N> w, final N n) {
    if (--maxSuggestions <= 0) {
      if (maxSuggestions == 0)
        System.out.println("Stopped logging suggestions");
      return;
    }
    if (logToFile) {
      init();
      output.put("File", fileName);
      output.put("Wring", clazz(w));
      output.put("Named", w.description());
      output.put("Kind", w.wringGroup());
      output.put("Described", w.description(n));
      output.put("Can suggest", w.canSuggest(n));
      output.put("Suggests", w.suggest(n));
      output.nl();
    }
    
    if (!logToScreen)
      return;
    
    if (logToScreen) {
        System.out.println("        File: " + fileName);
        System.out.println("       Wring: " + clazz(w));
        System.out.println("       Named: " + w.description());
        System.out.println("        Kind: " + w.wringGroup());
        System.out.println("   Described: " + w.description(n));
        System.out.println(" Can suggest: " + w.canSuggest(n));
        System.out.println("    Suggests: " + w.suggest(n));
    }
  }
  
  public static void activateLogToScreen(){
    logToScreen = true;
  }
  
  public static void activateLogToFile(){
    logToFile = true;
  }

  public static void visitation(final ASTNode ¢) {
    if (--maxVisitations <= 0) {
      if (maxVisitations == 0)
        System.out.println("Stopped logging visitations");
      return;
    }
    System.out.println("VISIT: '" + tide.clean(¢ + "") + "' [" + ¢.getLength() + "] (" + clazz(¢) + ")" + " parent = " + clazz(parent(¢)));
  }

  private static String clazz(final Object n) {
    return n.getClass().getSimpleName();
  }

  private static CSVStatistics init() {
    try {
      output = new CSVStatistics(outputDir, "Suggestions");
    } catch (final IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static void setFileName(String $) {
    fileName = $;    
  }

}
