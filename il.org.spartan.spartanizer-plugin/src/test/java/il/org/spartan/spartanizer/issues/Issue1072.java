package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

/** Tests of {@link WildcardTypeExtendsObjectTrim}
 * @author Yossi Gil
 * @since 2017-01-16 */
//
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue1072 {
  @Test public void a() {
    trimmingOf("class X extends Object{ }")//
        .gives("class X{}")//
        .stays();
  }
  @Test public void b() {
    trimmingOf("class X<T extends java.lang.Object> { }")//
        .gives("class X<T>{}")//
        .stays();
  }
  @Test public void c() {
    trimmingOf("public class X<T extends java.lang.Object> { List<? extends Object> list; }")//
        .gives("public class X<T> { List<?> list; }")//
        .stays();
  }
  @Test public void d() {
    trimmingOf("public class X <T extends y> { }")//
        .stays();
  }
  @Test public void e() {
    trimmingOf("public class X { List<? extends Object> list; }")//
        .gives("public class X { List<?> list; }")//
        .stays();
  }
  @Test public void f() {
    trimmingOf("abstract class X { abstract <T extends Object&C&Object&java.lang.Object&B&Object&A&Object&Object> T list();}")//
        .gives("abstract class X { abstract <T extends C&B&A> T list(); }")//
        .stays();
  }
}
