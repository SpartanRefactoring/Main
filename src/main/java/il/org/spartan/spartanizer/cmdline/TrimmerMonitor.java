package il.org.spartan.spartanizer.cmdline;

import static java.util.logging.Level.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.function.*;
import java.util.logging.*;
import java.util.logging.Formatter;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** A logging dash-board with auto-expiration of {@link Tipper} operations.
 * @author Yossi Gil
 * @since Sep 20, 2016 */
public class TrimmerMonitor extends Trimmer.With implements Trimmer.Tap {
  private int runs = 5;

  public static <T> T compute(final Supplier<T> $) {
    return $.get();
  }

  public static final Logger logger = compute(() -> {
    final Logger $ = Logger.getLogger(system.myCallerFullClassName());
    $.setUseParentHandlers(false);
    $.addHandler(compute(() -> {
      final ConsoleHandler $$ = new ConsoleHandler();
      $$.setFormatter(new Formatter() {
        @Override public String format(final LogRecord ¢) {
          return String.format("%2d. %s %s#%s %s: %s\n", //
              box.it(¢.getSequenceNumber()), //
              new SimpleDateFormat("hh:mm:ss").format(new Date(¢.getMillis())), //
              namer.lastComponent(¢.getSourceClassName()), //
              ¢.getSourceMethodName(), //
              ¢.getLevel(), //
              formatMessage(¢)//
          );
        }
      });
      $$.setLevel(ALL);
      return $$;
    }));
    return $;
  });

  boolean expired() {
    if (--runs == 0)
      System.out.println("Stopped logging applications");
    return runs > 0;
  }

  @Override public void setNode() {
    if (!expired())
      logger.log(FINEST, "Visit node {0}", current().node().getClass().getSimpleName());
  }

  @Override public void tipperAccepts() {
    if (!expired())
      logger.log(FINE, "Tipper {0} accepts node {1}", as.list(current().tipper(), current().node().getClass().getSimpleName()));
  }

  @Override public void tipperRejects() {
    if (!expired())
      logger.log(FINER, "Tipper %s rejects node %s", as.list(current().tipper(), current().node()));
  }

  @Override public void noTipper() {
    if (!expired())
      logger.log(FINER, "No tippers found for node {0}", current().node().getClass().getSimpleName());
  }

  @Override public void tipperTip() {
    if (!expired())
      logger.log(FINE, "Tipper %s generated tip %s for node %s", as.list(current().tipper(), current().tip(), current().node()));
  }

  @Override public void tipPrune() {
    if (!expired())
      logger.log(FINE, "Pruning re-%s", current().tip());
  }

  @Override public void tipRewrite() {
    if (!expired())
      logger.log(FINE, "Rewrite re-%s", current().rewrite());
  }

  public TrimmerMonitor(final Trimmer trimmer) {
    trimmer.super();
  }

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

  public static void activateLogToFile() {
    logToFile = true;
  }

  public static void activateLogToScreen() {
    logToScreen = true;
  }

  public static void rewrite(final ASTRewrite r, final Tip t) {
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
    TrimmerMonitor.maxApplications = maxApplications;
  }

  public static void setMaxTips(final int maxTips) {
    TrimmerMonitor.maxTips = maxTips;
  }

  public static void setMaxVisitations(final int maxVisitations) {
    TrimmerMonitor.maxVisitations = maxVisitations;
  }

  public static void setOutputDir(final String $) {
    TrimmerMonitor.outputDir = $;
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
