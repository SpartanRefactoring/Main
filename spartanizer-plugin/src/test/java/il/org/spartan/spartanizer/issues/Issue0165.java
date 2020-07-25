package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.junit.Test;

/** Unit tests for centification of a single parameter to a function
 * @author Yossi Gil
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public final class Issue0165 {
  @Test public void a01() {
    trimmingOf("public static boolean __final(final VariableDeclarationStatement $) {\nreturn (Modifier.FINAL & $.getModifiers()) != 0;}").stays();
  }
  @Test public void a02() {
    trimmingOf("public static boolean a(final V $) { return (Modifier.FINAL & $.getModifiers()) != 0;}").stays();
  }
  @Test public void a03() {
    trimmingOf("public static boolean a(final V $) { return (Modifier.FINAL & $.getModifiers()) != 0;}").stays();
  }
  @Test public void a04() {
    trimmingOf("boolean a(final V $) { return (Modifier.FINAL & $.getModifiers()) != 0;}").stays();
  }
  @Test public void a05() {
    trimmingOf("boolean a(V $) { return (Modifier.FINAL & $.getModifiers()) != 0;}").stays();
  }
  @Test public void a06() {
    trimmingOf("boolean a(V $) { return (F & $.g()) != 0;}")//
        .stays();
  }
  @Test public void a07() {
    trimmingOf("boolean a(V $) { return $.g() != 0;}").stays();
  }
  @Test public void a08() {
    trimmingOf("boolean a(int $) { return $.g() != 0;}").stays();
  }
  @Test public void a09() {
    trimmingOf("boolean a(int $) { return $ != 0;}").stays();
  }
  @Test public void a09a() {
    trimmingOf("boolean a(int $) { return $ > 0;}").stays();
  }
  @Test public void a09b() {
    trimmingOf("int a(int $) { return $ > 0;}").stays();
  }
  @Test public void a09c() {
    trimmingOf("int a(int $) { return $;}").stays();
  }
  @Test public void a09d() {
    trimmingOf("boolean a(int $) { return true;}").stays();
  }
  @Test public void a09da() {
    trimmingOf("void a(int $) {}").stays();
  }
  @Test public void a09db() {
    trimmingOf("void a(boolean $) {}").stays();
  }
  @Test public void a09dc() {
    trimmingOf("void a(T $) {}").stays();
  }
  @Test public void a09dd() {
    trimmingOf("void a(String $) {}").stays();
  }
  @Test public void a09de() {
    trimmingOf("void a() {String $ = \"ABC\"; $.hashCode();}").stays();
  }
  @Test public void a09e() {
    trimmingOf("boolean a(int b) { return $;}").stays();
  }
  @Test public void seriesA_01_vanilla() {
    trimmingOf("public static boolean f(final VariableDeclarationStatement s) {\nreturn (Modifier.FINAL & s.getModifiers()) != 0;}")
        .gives("public static boolean f(final VariableDeclarationStatement ¢) {\nreturn (Modifier.FINAL & ¢.getModifiers()) != 0;}");
  }
  @Test public void seriesA_03_single_underscore() {
    trimmingOf("void f(int _){}")//
        .gives("void f(int __){}")//
        .stays();
  }
  @Test public void seriesA_04_double_underscore() {
    trimmingOf("void f(int __){}")//
        .stays();
  }
  @Test public void seriesA_05_unused() {
    trimmingOf("void f(int a){}")//
        .stays();
  }
  @Test public void seriesA_06_abstract() {
    trimmingOf("abstract void f(int a);")//
        .stays();
  }
  @Test public void seriesA_06_meaningfulName() {
    trimmingOf("void f(String fileName) {f(fileName);}")//
        .stays();
  }
  @Test public void a11() {
    trimmingOf("(blkIds[off] & 0xFF) | ((extid & 0xF0) << 4)")//
        .gives("(extid&0xF0)<<4|blkIds[off]&0xFF") //
        .stays();
  }
}
