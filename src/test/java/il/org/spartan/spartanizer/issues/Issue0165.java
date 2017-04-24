package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Unit tests for centification of a single parameter to a function
 * @author Yossi Gil
 * @since 2016 */

@SuppressWarnings({ "static-method", "javadoc" })
public final class Issue0165 {
  @Test public void a01() {
    trimminKof("public static boolean __final(final VariableDeclarationStatement $) {\nreturn (Modifier.FINAL & $.getModifiers()) != 0;}").stays();
  }

  @Test public void a02() {
    trimminKof("public static boolean a(final V $) { return (Modifier.FINAL & $.getModifiers()) != 0;}").stays();
  }

  @Test public void a03() {
    trimminKof("public static boolean a(final V $) { return (Modifier.FINAL & $.getModifiers()) != 0;}").stays();
  }

  @Test public void a04() {
    trimminKof("boolean a(final V $) { return (Modifier.FINAL & $.getModifiers()) != 0;}").stays();
  }

  @Test public void a05() {
    trimminKof("boolean a(V $) { return (Modifier.FINAL & $.getModifiers()) != 0;}").stays();
  }

  @Test public void a06() {
    trimminKof("boolean a(V $) { return (F & $.g()) != 0;}")//
        .gives("boolean a(V $){return($.g()&F)!=0;}") //
        .stays();
  }

  @Test public void a07() {
    trimminKof("boolean a(V $) { return $.g() != 0;}").stays();
  }

  @Test public void a08() {
    trimminKof("boolean a(int $) { return $.g() != 0;}").stays();
  }

  @Test public void a09() {
    trimminKof("boolean a(int $) { return $ != 0;}").stays();
  }

  @Test public void a09a() {
    trimminKof("boolean a(int $) { return $ > 0;}").stays();
  }

  @Test public void a09b() {
    trimminKof("int a(int $) { return $ > 0;}").stays();
  }

  @Test public void a09c() {
    trimminKof("int a(int $) { return $;}").stays();
  }

  @Test public void a09d() {
    trimminKof("boolean a(int $) { return true;}").stays();
  }

  @Test public void a09da() {
    trimminKof("void a(int $) {}").stays();
  }

  @Test public void a09db() {
    trimminKof("void a(boolean $) {}").stays();
  }

  @Test public void a09dc() {
    trimminKof("void a(T $) {}").stays();
  }

  @Test public void a09dd() {
    trimminKof("void a(String $) {}").stays();
  }

  @Test public void a09de() {
    trimminKof("void a() {String $ = \"ABC\"; $.hashCode();}").stays();
  }

  @Test public void a09e() {
    trimminKof("boolean a(int b) { return $;}").stays();
  }

  @Test public void seriesA_01_vanilla() {
    trimminKof("public static boolean f(final VariableDeclarationStatement s) {\nreturn (Modifier.FINAL & s.getModifiers()) != 0;}")
        .gives("public static boolean f(final VariableDeclarationStatement ¢) {\nreturn (Modifier.FINAL & ¢.getModifiers()) != 0;}");
  }

  @Test public void seriesA_03_single_underscore() {
    trimminKof("void f(int _){}")//
        .gives("void f(int __){}")//
        .stays();
  }

  @Test public void seriesA_04_double_underscore() {
    trimminKof("void f(int __){}")//
        .stays();
  }

  @Test public void seriesA_05_unused() {
    trimminKof("void f(int a){}")//
        .stays();
  }

  @Test public void seriesA_06_abstract() {
    trimminKof("abstract void f(int a);")//
        .stays();
  }

  @Test public void seriesA_06_meaningfulName() {
    trimminKof("void f(String fileName) {f(fileName);}")//
        .stays();
  }

  @Test public void a11() {
    trimminKof("(blkIds[off] & 0xFF) | ((extid & 0xF0) << 4)")//
        .gives("(extid&0xF0)<<4|blkIds[off]&0xFF") //
        .stays();
  }
}
