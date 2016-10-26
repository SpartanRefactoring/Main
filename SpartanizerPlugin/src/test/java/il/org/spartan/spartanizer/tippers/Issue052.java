package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue052 {
  @Test public void A$a() {
    trimmingOf("abstract abstract interface a{}")//
        .gives("interface a{}")//
        .stays()//
    ;
  }

  @Test public void A$A() {
    trimmingOf("void m(){return;}")//
        .gives("void m(){}")//
        .stays()//
    ;
  }

  @Test public void A$A1() {
    trimmingOf("void m(){return a;}")//
        .stays();
  }

  @Test public void A$b() {
    trimmingOf("abstract interface a{}")//
        .gives("interface a{}")//
        .stays()//
    ;
  }

  @Test public void A$B1() {
    trimmingOf("void m(){if (a){f();return;}}")//
        .gives("void m(){if (a){f();;}}")//
        .gives("void m(){if (a) f();}")//
        .stays()//
    ;
  }

  @Test public void A$B2() {
    trimmingOf("void m(){if (a) ++i;else{f();return;}}")//
        .gives("void m(){if (a) ++i;else{f();;}}")//
        .gives("void m(){if (a) ++i;else f();}")//
        .stays()//
    ;
  }

  @Test public void A$c() {
    trimmingOf("interface a{}")//
        .stays();
  }

  @Test public void A$d() {
    trimmingOf("public interface A{public abstract void a();abstract void r();static final void s();}")
        .gives("public interface A{void a();void r();static void s();}")//
        .stays()//
    ;
  }

  @Test public void A$e() {
    trimmingOf("public interface A{static void remove();public static int i = 3;}").gives("public interface A{static void remove();int i = 3;}")//
        .stays()//
    ;
  }

  @Test public void A$f() {
    trimmingOf("public interface A{static void remove();public static int i;}").gives("public interface A{static void remove();int i;}")//
        .stays()//
    ;
  }

  @Test public void A$g() {
    trimmingOf("final class ClassTest{final void remove();}")//
        .gives("final class ClassTest{void remove();}")//
        .stays()//
    ;
  }

  @Test public void A$h() {
    trimmingOf("final class ClassTest{public final void remove();}").gives("final class ClassTest{public void remove();}")//
        .stays()//
    ;
  }

  @Test public void A$i() {
    trimmingOf("public final class ClassTest{static enum Day{SUNDAY,MONDAYSUNDAY,MONDAY}");
  }

  @Test public void A$j() {
    trimmingOf("public final class ClassTest{private static enum Day{SUNDAY,MONDAY}");
  }

  @Test public void A$k() {
    trimmingOf("public final class ClassTest{public  ClassTest(){}}")//
        .stays();
  }

  @Test public void A$l() {
    trimmingOf("abstract class A{final void f(){}}")//
        .stays();
  }

  @Test public void A$n() {
    trimmingOf("class A{public final static int i=3;}").gives("class A{public static final int i=3;}")//
        .stays();
  }

  @Test public void A$o() {
    trimmingOf("final class A{public final static int i = 3;}").gives("final class A{public static final int i = 3;}")//
        .stays();
  }

  @Test public void A$p() {
    trimmingOf("enum A{a1,a2;static enum B{b1,b2;static class C{static enum D{c1,c2}}}")
        .gives("enum A{a1,a2;enum B{b1,b2;static class C{static enum D{c1,c2}}}").gives("enum A{a1,a2;enum B{b1,b2;static class C{enum D{c1,c2}}}")//
        .stays()//
    ;
  }
}
