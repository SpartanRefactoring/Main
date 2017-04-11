package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for centification of a single parameter to a function
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public final class Issue0165 {
  @Test public void a01() {
    topDownTrimming("public static boolean __final(final VariableDeclarationStatement $) {\nreturn (Modifier.FINAL & $.getModifiers()) != 0;}").stays();
  }

  @Test public void a02() {
    topDownTrimming("public static boolean a(final V $) { return (Modifier.FINAL & $.getModifiers()) != 0;}").stays();
  }

  @Test public void a03() {
    topDownTrimming("public static boolean a(final V $) { return (Modifier.FINAL & $.getModifiers()) != 0;}").stays();
  }

  @Test public void a04() {
    topDownTrimming("boolean a(final V $) { return (Modifier.FINAL & $.getModifiers()) != 0;}").stays();
  }

  @Test public void a05() {
    topDownTrimming("boolean a(V $) { return (Modifier.FINAL & $.getModifiers()) != 0;}").stays();
  }

  @Test public void a06() {
    topDownTrimming("boolean a(V $) { return (F & $.g()) != 0;}")//
        .gives("boolean a(V $){return($.g()&F)!=0;}") //
        .stays();
  }

  @Test public void a07() {
    topDownTrimming("boolean a(V $) { return $.g() != 0;}").stays();
  }

  @Test public void a08() {
    topDownTrimming("boolean a(int $) { return $.g() != 0;}").stays();
  }

  @Test public void a09() {
    topDownTrimming("boolean a(int $) { return $ != 0;}").stays();
  }

  @Test public void a09a() {
    topDownTrimming("boolean a(int $) { return $ > 0;}").stays();
  }

  @Test public void a09b() {
    topDownTrimming("int a(int $) { return $ > 0;}").stays();
  }

  @Test public void a09c() {
    topDownTrimming("int a(int $) { return $;}").stays();
  }

  @Test public void a09d() {
    topDownTrimming("boolean a(int $) { return true;}").stays();
  }

  @Test public void a09da() {
    topDownTrimming("void a(int $) {}").stays();
  }

  @Test public void a09db() {
    topDownTrimming("void a(boolean $) {}").stays();
  }

  @Test public void a09dc() {
    topDownTrimming("void a(T $) {}").stays();
  }

  @Test public void a09dd() {
    topDownTrimming("void a(String $) {}").stays();
  }

  @Test public void a09de() {
    topDownTrimming("void a() {String $ = \"ABC\"; $.hashCode();}").stays();
  }

  @Test public void a09e() {
    topDownTrimming("boolean a(int b) { return $;}").stays();
  }

  @Test public void seriesA_01_vanilla() {
    topDownTrimming("public static boolean f(final VariableDeclarationStatement s) {\nreturn (Modifier.FINAL & s.getModifiers()) != 0;}")
        .gives("public static boolean f(final VariableDeclarationStatement ¢) {\nreturn (Modifier.FINAL & ¢.getModifiers()) != 0;}");
  }

  @Test public void seriesA_03_single_underscore() {
    topDownTrimming("void f(int _){}")//
        .gives("void f(int __){}")//
        .stays();
  }

  @Test public void seriesA_04_double_underscore() {
    topDownTrimming("void f(int __){}")//
        .stays();
  }

  @Test public void seriesA_05_unused() {
    topDownTrimming("void f(int a){}")//
        .stays();
  }

  @Test public void seriesA_06_abstract() {
    topDownTrimming("abstract void f(int a);")//
        .stays();
  }

  @Test public void seriesA_06_meaningfulName() {
    topDownTrimming("void f(String fileName) {f(fileName);}")//
        .stays();
  }

  @Test public void a11() {
    topDownTrimming("(blkIds[off] & 0xFF) | ((extid & 0xF0) << 4)")//
        .gives("(extid&0xF0)<<4|blkIds[off]&0xFF") //
        .stays();
  }
}
