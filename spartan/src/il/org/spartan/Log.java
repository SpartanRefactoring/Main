package il.org.spartan;

import java.io.*;
import java.text.*;
import java.util.*;

import org.eclipse.jdt.annotation.*;

import il.org.spartan.bench.*;
import il.org.spartan.strings.*;
import il.org.spartan.utils.*;
import fluent.ly.*;

/** @author Yossi Gil
 * @since 04/06/2011 */
public enum Log {
  ;
  private static int level;
  private static boolean active = true;
  private static PrintStream out = System.out;
   private static final Tab tabber = new Tab("  ");
  private static int maxLevel = 100;
  private static final Stack<Stopwatch> stack = new Stack<>();

  public static void activate() {
    active = true;
  }
  public static void beginCompoundStage(final Object... ¢) {
    beginStage(¢);
    increaseLevel();
  }
  public static void beginStage( final Object... ¢) {
    beginStage(Separate.by(¢, " "));
  }
  public static void beginStage(final String stage) {
    Log.ln("Begin:", stage);
    stack.push(new Stopwatch(stage).start());
  }
  public static void deactivate() {
    active = false;
  }
  public static void decreaseLevel() {
    ___.require(level > 0);
    --level;
    tabber.less();
    ___.sure(level >= 0);
  }
  public static void endCompoundStage(final Object... ss) {
    decreaseLevel();
    endStage(ss);
  }
  public static void endStage( final Object... ss) {
    @SuppressWarnings("null")  final Stopwatch s = stack.pop().stop();
    Log.ln("End:", s.name(), StringUtils.paren(s) + ";", Separate.by(ss, " "));
  }
  public static void f( final String format, final Object... os) {
    ln(String.format(format, os));
  }
  public static void flush() {
    out.flush();
  }
  public static int getMaxLevel() {
    return maxLevel;
  }
  public static void increaseLevel() {
    tabber.more();
    ++level;
  }
  public static void ln( final Object... ¢) {
    print(prefix() + Separate.by(¢, " ") + "\n");
  }
  public static boolean logging() {
    return active && level <= maxLevel;
  }
  public static String now() {
    return new SimpleDateFormat("HH:mm:ss ").format(Calendar.getInstance().getTime());
  }
  public static void print(final Object ¢) {
    if (!logging())
      return;
    out.print(¢);
    flush();
  }
  public static void setLogStream(final PrintStream log) {
    out = log;
  }
  public static void setMaxLevel(final int maxLevel) {
    Log.maxLevel = maxLevel;
  }
   private static String prefix() {
    return now() + tabber + "";
  }
}
