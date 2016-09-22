package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue052Test {
  @Test public void A$a() {
    trimmingOf("abstract abstract interface a"//
        + "{}").gives("interface a {}");
  }

  @Test public void A$A() {
    trimmingOf( //
        "void m() { return; }").gives("void m() {}");
  }

  @Test public void A$A1() {
    trimmingOf( //
        "void m() { return a; }").stays();
  }

  @Test public void A$b() {
    trimmingOf("abstract interface a"//
        + "{}").gives("interface a {}");
  }

  @Test public void A$B1() {
    trimmingOf( //
        "void m() { if (a) { f(); return; }}").gives("void m() { if (a) { f(); ; }}");
  }

  @Test public void A$B2() {
    trimmingOf( //
        "void m() { if (a) ++i; else { f(); return; }}").gives("void m() { if (a) ++i; else { f(); ; }}");
  }

  @Test public void A$c() {
    trimmingOf("interface a"//
        + "{}").stays();
  }

  @Test public void A$d() {
    trimmingOf(//
        "public interface A {\n"//
            + "public abstract void add();\n"//
            + "abstract void remove()\n; "//
            + "static final void remove()\n; "//
            + "}"//
    ).gives("public interface A {\n" + "void add();\n" + "void remove()\n; " + "static void remove()\n; " + "}");
  }

  @Test public void A$e() {
    trimmingOf(//
        "public interface A {\n"//
            + "static void remove()\n; "//
            + "public static int i = 3\n; "//
            + "}")
                .gives("public interface A {\n"//
                    + "static void remove()\n; "//
                    + "int i = 3\n; "//
                    + "}");
  }

  @Test public void A$f() {
    trimmingOf(//
        "public interface A {\n"//
            + "static void remove()\n; "//
            + "public static int i\n; "//
            + "}")
                .gives("public interface A {\n"//
                    + "static void remove()\n; "//
                    + "int i\n; "//
                    + "}");
  }

  @Test public void A$g() {
    trimmingOf("final class ClassTest {\n"//
        + "final void remove();\n"//
        + "}")
            .gives("final class ClassTest {\n"//
                + "void remove();\n "//
                + "}");
  }

  @Test public void A$h() {
    trimmingOf("final class ClassTest {\n"//
        + "public final void remove();\n"//
        + "}"//
    ).gives(//
        "final class ClassTest {\n"//
            + "public void remove();\n "//
            + "}");
  }

  @Test public void A$i() {
    trimmingOf("public final class ClassTest {\n"//
        + "static enum Day {\n"//
        + "SUNDAY, MONDAY\n"//
        + "SUNDAY, MONDAY\n"//
        + "}");
  }

  @Test public void A$j() {
    trimmingOf("public final class ClassTest {\n"//
        + "private static enum Day {\n"//
        + "SUNDAY, MONDAY\n"//
        + "}");
  }

  @Test public void A$k() {
    trimmingOf("public final class ClassTest {\n"//
        + "public  ClassTest(){}\n"//
        + "}").stays();
  }

  @Test public void A$l() {
    trimmingOf("abstract class A { final void f() { }}").stays();
  }

  @Test public void A$n() {
    trimmingOf(//
        "abstract class A {\n"//
            + "static void f() {}\n "//
            + "public final static int i = 3; "//
            + "}")
                .gives(//
                    "abstract class A {\n"//
                        + "static void f() {}\n "//
                        + "public static final int i = 3; "//
                        + "}")
                .stays();
  }

  @Test public void A$o() {
    trimmingOf(//
        "final class A {\n"//
            + "static void f() {}\n "//
            + "public final static int i = 3; "//
            + "}")//
                .gives(//
                    "final class A {\n"//
                        + "static void f() {}\n "//
                        + "public static final int i = 3; "//
                        + "}")//
                .stays();
  }

  @Test public void A$p() {
    trimmingOf(//
        "enum A {a1, a2; static enum B {b1, b2; static class C { static enum D {c1, c2}}}")//
            .gives("enum A {a1, a2; enum B {b1, b2; static class C { static enum D {c1, c2}}}")//
            .gives("enum A {a1, a2; enum B {b1, b2; static class C { enum D {c1, c2}}}");
  }
}
