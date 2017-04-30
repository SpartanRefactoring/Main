package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Unit tests for the GitHub issue thus numbered
 * @author Yossi Gil
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0052 {
  @Test public void A$a() {
    trimmingOf("abstract abstract interface a{}")//
        .gives("abstract interface a{}")//
        .gives("interface a{}")//
        .stays() //
    ;
  }

  @Test public void A$A() {
    trimmingOf("void m(){return;}")//
        .gives("void m(){}")//
        .stays() //
    ;
  }

  @Test public void A$A1() {
    trimmingOf("void m(){return a;}")//
        .stays() //
    ;
  }

  @Test public void A$b() {
    trimmingOf("abstract interface a{}")//
        .gives("interface a{}")//
        .stays() //
    ;
  }

  @Test public void A$B1() {
    trimmingOf("void m(){if (a){f(); return;}}")//
        .gives("void m(){if (a){f();;}}")//
        .gives("void m(){if (a)f();}")//
        .stays() //
    ;
  }

  @Test public void A$B1a() {
    trimmingOf("void m(){if (a){f(); return;}}")//
        .gives("void m(){if (a){f();;}}")//
    ;
  }

  @Test public void A$B2() {
    trimmingOf("void m(){if (a) ++i; else{f(); return;}}")//
        .gives("void m(){if (a) ++i; else{f(); ;}}")//
        .gives("void m(){if (a) ++i; else f();}")//
        .stays() //
    ;
  }

  @Test public void A$c() {
    trimmingOf("interface a{}")//
        .stays();
  }

  @Test public void A$d() {
    trimmingOf("public interface A{public abstract void a();abstract void r();static final void s();}")
        .gives("public interface A{void a();void r();static void s();}")//
        .stays() //
    ;
  }

  @Test public void A$e() {
    trimmingOf("public interface A{static void remove() ; public static int i = 3 ;}")//
        .gives("public interface A{static void remove() ; int i = 3 ;}")//
        .stays() //
    ;
  }

  @Test public void A$f() {
    trimmingOf("public interface A{static void remove() ; public static int i ;}")//
        .gives("public interface A{static void remove() ; int i ;}")//
        .stays() //
    ;
  }

  @Test public void A$g() {
    trimmingOf("final class ClassTest{final void remove();}")//
        .gives("final class ClassTest{void remove();}")//
        .stays() //
    ;
  }

  @Test public void A$h() {
    trimmingOf("final class ClassTest{public final void remove();}")//
        .gives("final class ClassTest{public void remove();}")//
        .stays() //
    ;
  }

  @Test public void A$i() {
    trimmingOf("public final class ClassTest{static enum Day{SUNDAY, MONDAY, SUNDAY, MONDAY}}") //
        .gives("public final class ClassTest{enum Day{SUNDAY, MONDAY, SUNDAY, MONDAY}}") //
        .stays() //
    ;
  }

  @Test public void A$j() {
    trimmingOf("public final class ClassTest{private static enum Day{SUNDAY, MONDAY}") //
        .gives("public final class ClassTest{private enum Day{SUNDAY, MONDAY}")//
        .stays() //
    ;
  }

  @Test public void A$k() {
    trimmingOf("public final class ClassTest{public ClassTest(){}}")//
        .gives("public final class ClassTest{}")//
        .stays();
  }

  @Test public void A$l() {
    trimmingOf("abstract class A{final void f(){}}")//
        .stays();
  }

  @Test public void A$n() {
    trimmingOf("abstract class A{static void f(){}public final static int i = 3;}")
        .gives("abstract class A{static void f(){}public static final int i = 3;}")//
        .stays();
  }

  @Test public void A$o() {
    trimmingOf("final class A{static void f(){}public final static int i = 3;}")
        .gives("final class A{static void f(){}public static final int i = 3;}")//
        .stays()//
    ;
  }

  @Test public void A$p() {
    trimmingOf("enum A{y,x;static enum B{b,v;static class C{static enum D{c,w}}}")//
        .gives("enum A{y,x;enum B{b,v;static class C{enum D{c,w}}}")//
        .stays()//
    ;
  }

  @Test public void B$a() {
    trimmingOf("interface a{static int a = 1;}")//
        .gives("interface a{int a = 1;}")//
        .stays() //
    ;
  }

  @Test public void B$b() {
    trimmingOf("interface a{ final int a = 1;}")//
        .gives("interface a{int a = 1;}")//
        .stays() //
    ;
  }

  @Test public void B$c() {
    trimmingOf("interface a{ static final int a = 1;}")//
        .gives("interface a{int a = 1;}")//
        .stays() //
    ;
  }

  @Test public void C$a() {
    trimmingOf("interface a{static class A{}}")//
        .gives("interface a{class A{}}")//
        .stays() //
    ;
  }

  @Test public void C$b() {
    trimmingOf("interface a{static interface A{}}")//
        .gives("interface a{interface A{}}")//
        .stays() //
    ;
  }

  @Test public void C$c() {
    trimmingOf("interface a{static enum A{}}")//
        .gives("interface a{enum A{}}")//
        .stays() //
    ;
  }

  @Test public void C$d() {
    trimmingOf("public interface C{static class A{}}")//
        .gives("public interface C{class A{}}")//
        .stays() //
    ;
  }

  void m() {
    if (new Object().hashCode() == 0)
      m();
  }
}
