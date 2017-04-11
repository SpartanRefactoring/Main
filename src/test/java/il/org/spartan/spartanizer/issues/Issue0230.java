package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.tippers.*;

/** Unit tests for {@link BodyDeclarationModifiersSort}
 * @author Alex Kopzon
 * @since 2016-09 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0230 {
  @Test public void a() {
    topDownTrimming("protected public final class A{volatile static int a;}")//
        .gives("public protected final class A{volatile static int a;}").gives("public protected final class A{static volatile int a;}")//
        .stays();
  }

  @Test public void a1() {
    topDownTrimming("class A{volatile volatile int a;}")//
        .gives("class A{volatile int a;}")//
        .stays();
  }

  @Test public void a11() {
    topDownTrimming("class A{@UserDefined1 int a;}")//
        .stays();
  }

  @Test public void a111() {
    topDownTrimming("class A{@Deprecated int a;}")//
        .stays();
  }

  @Test public void a2() {
    topDownTrimming("private T value = null;")//
        .gives("") //
        .stays();
  }

  @Test public void a35() {
    topDownTrimming("class A{@UserDefined1 @UserDefined2 @Override int f() {}}")//
        .gives("class A{@Override @UserDefined1 @UserDefined2 int f() {}}")//
        .stays();
  }

  @Test public void a36() {
    topDownTrimming("class A{@UserDefined1 @UserDefined2 int f() {}}")//
        .stays();
  }

  @Test public void b() {
    topDownTrimming("protected public final class A{volatile static int a;}")//
        .gives("public protected final class A{volatile static int a;}").gives("public protected final class A{static volatile int a;}")//
        .stays();
  }

  @Test public void c() {
    topDownTrimming("protected public final @Deprecated class A{volatile static int a;}")
        .gives("@Deprecated public protected final class A{volatile static int a;}")
        .gives("@Deprecated public protected final class A{static volatile int a;}")//
        .stays();
  }

  @Test public void d() {
    topDownTrimming("protected public final @Deprecated class A{volatile static @SuppressWarnings(\"deprecation\") int a;}")
        .gives("@Deprecated public protected final class A{volatile static @SuppressWarnings(\"deprecation\") int a;}")
        .gives("@Deprecated public protected final class A{@SuppressWarnings(\"deprecation\") static volatile int a;}")//
        .stays();
  }

  @Test public void h() {
    topDownTrimming("@Retention(RetentionPolicy.RUNTIME)@Target({ElementType.METHOD})public @interface Tweezable {}")//
        .gives("@Retention(RetentionPolicy.RUNTIME)@Target(ElementType.METHOD)public @interface Tweezable{}") //
        .stays();
  }

  @Test public void i() {
    topDownTrimming("public @interface hand_made { String[] value(); }@hand_made({}) final  String s = \"a\";").stays();
  }
}
