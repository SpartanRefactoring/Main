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
    trimminKof("public final class A{static public int a;}")//
        .gives("public final class A{public static int a;}")//
        .stays();
  }

  @Test public void A$a_1a() {
    trimminKof("public final class A{public static int a;}")//
        .stays();
  }

  @Test public void A$a_1a1() {
    trimminKof("final public class A{}")//
        .gives("public final class A{}")//
        .stays();
  }

  @Test public void A$a_1a2() {
    trimminKof("public final class A{}")//
        .stays();
  }

  @Test public void A$b_1() {
    trimminKof("public final class A{static final public int a;}")//
        .gives("public final class A{public static final int a;}")//
        .stays();
  }

  @Test public void A$c() {
    trimminKof("protected public void f();")//
        .gives("public protected void f();")//
        .stays();
  }

  @Test public void A$c_2() {
    trimminKof("public final class A{synchronized public void fun(final int __){} final private String s = \"Alex\";}")
        .gives("public final class A{public synchronized void fun(final int __){} private final String s = \"Alex\";}")//
        .stays();
  }

  @Test public void A$d() {
    trimminKof("protected public final class A{}")//
        .gives("public protected final class A{}")//
        .stays();
  }

  @Test public void A$d_1() {
    trimminKof("abstract class A{}")//
        .stays();
  }

  @Test public void A$e() {
    trimminKof("protected public final class A{volatile static int a;}")//
        .gives("public protected final class A{volatile static int a;}")//
        .gives("public protected final class A{static volatile int a;}")//
        .stays();
  }

  @Test public void A$g() {
    trimminKof("protected public final public enum Level{HIGH, MEDIUM, LOW}")//
        .gives("public protected final enum Level{HIGH, MEDIUM, LOW}")//
        .gives("public protected enum Level{HIGH, MEDIUM, LOW}")//
        .stays();
  }

  public void A$h() {
    trimminKof("protected public int a;")//
        .gives("public protected int a;")//
        .stays();
  }

  public void A$i() {
    trimminKof("protected public int a;")//
        .gives("public protected int a;")//
        .stays();
  }

  public void A$q() {
    trimminKof("protected public int a;")//
        .gives("public protected int a;")//
        .stays();
  }

  public void A$w() {
    trimminKof("protected public int a;")//
        .gives("public protected int a;")//
        .stays();
  }

  public void A$y() {
    trimminKof("synchronized volatile public int a;")//
        .gives("public volatile synchronized int a;")//
        .stays();
  }

  public void A$z() {
    trimminKof("volatile private int a;")//
        .gives("private volatile int a;")//
        .stays();
  }
}
