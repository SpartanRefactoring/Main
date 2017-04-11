package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;
import org.junit.runners.*;

/** Tests for the GitHub issue thus numbered
 * @author Alex Kopzon
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue0111 {
  @Test public void A$a_1() {
    topDownTrimming("public final class A{static public int a;}")//
        .gives("public final class A{public static int a;}")//
        .stays();
  }

  @Test public void A$a_1a() {
    topDownTrimming("public final class A{public static int a;}")//
        .stays();
  }

  @Test public void A$a_1a1() {
    topDownTrimming("final public class A{}")//
        .gives("public final class A{}")//
        .stays();
  }

  @Test public void A$a_1a2() {
    topDownTrimming("public final class A{}")//
        .stays();
  }

  @Test public void A$b_1() {
    topDownTrimming("public final class A{static final public int a;}")//
        .gives("public final class A{public static final int a;}")//
        .stays();
  }

  @Test public void A$c() {
    topDownTrimming("protected public void f();")//
        .gives("public protected void f();")//
        .stays();
  }

  @Test public void A$c_2() {
    topDownTrimming("public final class A{synchronized public void fun(final int __){} final private String s = \"Alex\";}")
        .gives("public final class A{public synchronized void fun(final int __){} private final String s = \"Alex\";}")//
        .stays();
  }

  @Test public void A$d() {
    topDownTrimming("protected public final class A{}")//
        .gives("public protected final class A{}")//
        .stays();
  }

  @Test public void A$d_1() {
    topDownTrimming("abstract class A{}")//
        .stays();
  }

  @Test public void A$e() {
    topDownTrimming("protected public final class A{volatile static int a;}")//
        .gives("public protected final class A{volatile static int a;}")//
        .gives("public protected final class A{static volatile int a;}")//
        .stays();
  }

  @Test public void A$g() {
    topDownTrimming("protected public final public enum Level{HIGH, MEDIUM, LOW}")//
        .gives("public protected final enum Level{HIGH, MEDIUM, LOW}")//
        .gives("public protected enum Level{HIGH, MEDIUM, LOW}")//
        .stays();
  }

  public void A$h() {
    topDownTrimming("protected public int a;")//
        .gives("public protected int a;")//
        .stays();
  }

  public void A$i() {
    topDownTrimming("protected public int a;")//
        .gives("public protected int a;")//
        .stays();
  }

  public void A$q() {
    topDownTrimming("protected public int a;")//
        .gives("public protected int a;")//
        .stays();
  }

  public void A$w() {
    topDownTrimming("protected public int a;")//
        .gives("public protected int a;")//
        .stays();
  }

  public void A$y() {
    topDownTrimming("synchronized volatile public int a;")//
        .gives("public volatile synchronized int a;")//
        .stays();
  }

  public void A$z() {
    topDownTrimming("volatile private int a;")//
        .gives("private volatile int a;")//
        .stays();
  }
}
