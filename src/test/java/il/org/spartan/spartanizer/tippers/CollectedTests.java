package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** * Unit tests for the nesting class Unit test for the containing class. Note
 * our naming convention: a) test methods do not use the redundant "test"
 * prefix. b) test methods begin with the name of the method they check.
 * @author Yossi Gil {@code yossi dot (optional) gil at gmail dot (required) com}
 * @since 2014-07-10 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public final class CollectedTests {
  @Test public void a() {
    trimmingOf(
        "   final Collection<Integer> outdated = new ArrayList<>();     int x = 6, y = 7;     S.x.f(x+y);     final Collection<Integer> coes = new ArrayList<>();     for (final Integer pi : coes)      if (pi.intValue() <x - y)       outdated.add(pi);     for (final Integer pi : outdated)      coes.remove(pi);     S.x.f(coes.size()); ")
            .stays();
  }

  @Test public void b() {
    trimmingOf("int a  = f(); for (int i = a; i <100; i++) b[i] = 3;")//
        .gives(" for (int i = f(); i <100; i++) b[i] = 3;");
  }

  @Test public void c() {
    trimmingOf(
        "   final Collection<Integer> outdated = new ArrayList<>();     int x = 6, y = 7;     S.x.f(x+y);     final Collection<Integer> coes = new ArrayList<>();     for (final Integer pi : coes)      if (pi.intValue() <x - y)       outdated.add(pi);     S.x.f(coes.size());     S.x.f(outdated.size()); ")
            .stays();
  }

  @Test public void d() {
    trimmingOf("    final W s = new W(\"bob\");\n" + "    return s.l(hZ).l(\"-ba\").toString() == \"bob-ha-banai\";")
        .gives("return(new W(\"bob\")).l(hZ).l(\"-ba\").toString()==\"bob-ha-banai\";");
  }

  @Test public void e() {
    trimmingOf("1-c-b")//
        .gives("1-b-c");
  }

  @Test public void f() {
    trimmingOf("int res = 0;   String $ = blah + known;   y(res + $.length());   return res + $.length();")//
        .stays();
  }

  @Test public void g() {
    trimmingOf("")//
        .stays();
  }

  @Test public void h() {
    trimmingOf("public void f() {\n")//
        .stays();
  }

  @Test public void i() {
    trimmingOf("switch (a) { default: }")//
        .gives("");
  }

  @Test public void shortestOperand09() {
    trimmingOf("return 2 - 4 <50 - 20 - 10 - 5;")//
        .gives("return  -2<15;") //
        .stays();
  }
}