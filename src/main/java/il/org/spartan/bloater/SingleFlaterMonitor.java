package il.org.spartan.bloater;

import static java.util.logging.Level.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.logging.*;
import java.util.logging.Formatter;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;
import il.org.spartan.utils.fluent.*;

/** A logging dash-board with auto-expiration of {@link Tipper} operations.
 * @author Oran Gilboa
 * @since 2017-04-13 */
public class SingleFlaterMonitor extends SingleFlater.With implements SingleFlater.Tap {
  public static void off() {
    logger.setLevel(Level.OFF);
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

  public static void setFileName(final String $) {
    fileName = $;
  }

  public static void setMaxApplications(final int maxApplications) {
    SingleFlaterMonitor.maxApplications = maxApplications;
  }

  public static void setMaxTips(final int maxTips) {
    SingleFlaterMonitor.maxTips = maxTips;
  }

  public static void setMaxVisitations(final int maxVisitations) {
    SingleFlaterMonitor.maxVisitations = maxVisitations;
  }

  public static void setOutputDir(final String $) {
    SingleFlaterMonitor.outputDir = $;
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
      return note.io($);
    }
  }

  public static final Logger logger = anonymous.ly(() -> {
    final Logger $ = Logger.getLogger(system.myCallerFullClassName());
    $.setUseParentHandlers(false);
    $.addHandler(anonymous.ly(() -> {
      final ConsoleHandler $$ = new ConsoleHandler();
      $.setUseParentHandlers(false);
      $.setLevel(Level.ALL);
      $$.setFormatter(new Formatter() {
        @Override public String format(final LogRecord ¢) {
          return String.format("%2d. %s %s#%s %s: %s\n", //
              box.it(¢.getSequenceNumber()), //
              new SimpleDateFormat("hh:mm:ss").format(new Date(¢.getMillis())), //
              Namer.lastComponent(¢.getSourceClassName()), //
              ¢.getSourceMethodName(), //
              ¢.getLevel(), //
              formatMessage(¢)//
          );
        }
      });
      return $$;
    }));
    return $;
  });
  private static String fileName;
  private static boolean logToFile;
  private static boolean logToScreen = true; // default output
  private static int maxApplications = 10;
  private static int maxTips = 20;
  private static int maxVisitations = 30;
  private static CSVStatistics output;
  private static String outputDir = "/tmp/trimmerlog-output.CSV";
  static {
    logToScreen = true;
    off();
  }

  public SingleFlaterMonitor(final SingleFlater singleFlater) {
    singleFlater.super();
  }

  @Override public void noTipper() {
    logger.log(FINER, "No tippers for {0}", node());
  }

  @Override public void setNode() {
    logger.log(FINEST, "Visit {0}", node());
  }

  @Override public void tipperAccepts() {
    logger.log(FINE, "{0} accepts {1}", as.array(tipper(), node()));
  }

  @Override public void tipperRejects() {
    logger.log(FINER, "{0} rejects {1}", as.array(tipper(), node()));
  }

  @Override public void tipperTip() {
    logger.log(FINE, "{0} tips {1}", as.array(tipper(), tip(), node()));
  }

  @Override public void tipRewrite() {
    logger.log(FINE, "Rewrite {0}", current().rewrite());
  }

  private String node() {
    final ASTNode $ = current().node();
    return String.format("%s(%s)", English.name($), Trivia.gist($));
  }

  private Tip tip() {
    return current().tip();
  }

  private String tipper() {
    return English.name(current().tipper());
  }
}
