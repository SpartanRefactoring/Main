package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Tests for the GitHub issue thus numbered
 * @author Alex Kopzon
 * @since 2016 */
//
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue0111 {
  @Test public void A$a_1() {
    trimmingOf("public final class A{static public int a;}")//
        .gives("public final class A{public static int a;}")//
        .stays();
  }
  @Test public void A$a_1a() {
    trimmingOf("public final class A{public static int a;}")//
        .stays();
  }
  @Test public void A$a_1a1() {
    trimmingOf("final public class A{}")//
        .gives("public final class A{}")//
        .stays();
  }
  @Test public void A$a_1a2() {
    trimmingOf("public final class A{}")//
        .stays();
  }
  @Test public void A$b_1() {
    trimmingOf("public final class A{static final public int a;}")//
        .gives("public final class A{public static final int a;}")//
        .stays();
  }
  @Test public void A$c() {
    trimmingOf("protected public void f();")//
        .gives("public protected void f();")//
        .stays();
  }
  @Test public void A$c_2() {
    trimmingOf("public final class A{synchronized public void fun(final int __){} final private String s = \"Alex\";}")
        .gives("public final class A{public synchronized void fun(final int __){} private final String s = \"Alex\";}")//
        .stays();
  }
  @Test public void A$d() {
    trimmingOf("protected public final class A{}")//
        .gives("public protected final class A{}")//
        .stays();
  }
  @Test public void A$d_1() {
    trimmingOf("abstract class A{}")//
        .stays();
  }
  @Test public void A$e() {
    trimmingOf("protected public final class A{volatile static int a;}")//
        .gives("public protected final class A{volatile static int a;}")//
        .gives("public protected final class A{static volatile int a;}")//
        .stays();
  }
  @Test public void A$g() {
    trimmingOf("protected public final public enum Level{HIGH, MEDIUM, LOW}")//
        .gives("public protected final enum Level{HIGH, MEDIUM, LOW}")//
        .gives("public protected enum Level{HIGH, MEDIUM, LOW}")//
        .stays();
  }
  public void A$h() {
    trimmingOf("protected public int a;")//
        .gives("public protected int a;")//
        .stays();
  }
  public void A$i() {
    trimmingOf("protected public int a;")//
        .gives("public protected int a;")//
        .stays();
  }
  public void A$q() {
    trimmingOf("protected public int a;")//
        .gives("public protected int a;")//
        .stays();
  }
  public void A$w() {
    trimmingOf("protected public int a;")//
        .gives("public protected int a;")//
        .stays();
  }
  public void A$y() {
    trimmingOf("synchronized volatile public int a;")//
        .gives("public volatile synchronized int a;")//
        .stays();
  }
  public void A$z() {
    trimmingOf("volatile private int a;")//
        .gives("private volatile int a;")//
        .stays();
  }
}
