package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;
import org.junit.runners.*;

/** * Unit tests for the nesting class Unit test for the containing class. Note
 * our naming convention: a) test methods do not use the redundant "test"
 * prefix. b) test methods begin with the name of the method they check.
 * @author Yossi Gil
 * @since 2014-07-10 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public final class CollectedTests {
  @Test public void a() {
    trimminKof(
        "   final Collection<Integer> outdated = an.empty.list();     int x = 6, y = 7;     S.x.f(x+y);     final Collection<Integer> coes = an.empty.list();     for (final Integer pi : coes)      if (pi.intValue() <x - y)       outdated.add(pi);     for (final Integer pi : outdated)      coes.remove(pi);     S.x.f(coes.size()); ")
            .stays();
  }

  @Test public void b() {
    trimminKof("int a  = f(); for (int i = a; i <100; i++) b[i] = 3;")//
        .gives("for (int i = f(); i <100; i++) b[i] = 3;");
  }

  @Test public void c() {
    trimminKof(
        "   final Collection<Integer> outdated = an.empty.list();     int x = 6, y = 7;     S.x.f(x+y);     final Collection<Integer> coes = an.empty.list();     for (final Integer pi : coes)      if (pi.intValue() <x - y)       outdated.add(pi);     S.x.f(coes.size());     S.x.f(outdated.size()); ")
            .stays();
  }

  @Test public void d() {
    trimminKof("   final W s = new W(\"bob\");\n    return s.l(hZ).l(\"-ba\").toString() == \"bob-ha-banai\";")
        .gives("return(new W(\"bob\")).l(hZ).l(\"-ba\").toString()==\"bob-ha-banai\";");
  }

  @Test public void e() {
    trimminKof("1-c-b")//
        .gives("1-b-c");
  }

  @Test public void f() {
    trimminKof("int res = 0;   String $ = blah + known;   y(res + $.length());   return res + $.length();")//
        .stays();
  }

  @Test public void g() {
    trimminKof("")//
        .stays();
  }

  @Test public void h() {
    trimminKof("public void f() {\n")//
        .stays();
  }

  @Test public void i() {
    trimminKof("switch (a) { default: }")//
        .gives("");
  }

  @Test public void shortestOperand09() {
    trimminKof("return 2 - 4 <50 - 20 - 10 - 5;")//
        .gives("return  -2<15;") //
        .stays();
  }
}