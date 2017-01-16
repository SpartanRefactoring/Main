package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for {@link $BodyDeclarationModifiersSort}
 * @author Alex Kopzon
 * @since 2016-09 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0230 {
  @Test public void a() {
    trimmingOf("protected public final class A{volatile static int a;}")//
        .gives("public protected final class A{volatile static int a;}").gives("public protected final class A{static volatile int a;}")//
        .stays();
  }

  @Test public void a1() {
    trimmingOf("class A{volatile volatile int a;}")//
        .gives("class A{volatile int a;}")//
        .stays();
  }

  @Test public void a11() {
    trimmingOf("class A{@UserDefined1 @UserDefined1 int a;}")//
        .stays();
  }

  @Test public void a111() {
    trimmingOf("class A{@Deprecated @Deprecated int a;}")//
        .stays();
  }

  @Test public void a2() {
    trimmingOf("@Nullable private T value = null;")//
        .stays();
  }

  @Test public void a35() {
    trimmingOf("class A{@UserDefined1 @UserDefined2 @Override int f() {}}")//
        .gives("class A{@Override @UserDefined1 @UserDefined2 int f() {}}")//
        .stays();
  }

  @Test public void a36() {
    trimmingOf("class A{@UserDefined1 @UserDefined2 int f() {}}")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("protected public final class A{volatile static int a;}")//
        .gives("public protected final class A{volatile static int a;}").gives("public protected final class A{static volatile int a;}")//
        .stays();
  }

  @Test public void c() {
    trimmingOf("protected public final @Deprecated class A{volatile static int a;}")
        .gives("@Deprecated public protected final class A{volatile static int a;}")
        .gives("@Deprecated public protected final class A{static volatile int a;}")//
        .stays();
  }

  @Test public void d() {
    trimmingOf("protected public final @Deprecated class A{volatile static @SuppressWarnings(\"deprecation\") int a;}")
        .gives("@Deprecated public protected final class A{volatile static @SuppressWarnings(\"deprecation\") int a;}")
        .gives("@Deprecated public protected final class A{@SuppressWarnings(\"deprecation\") static volatile int a;}")//
        .stays();
  }

  @Test public void h() {
    trimmingOf("@Retention(RetentionPolicy.RUNTIME)@Target({ElementType.METHOD})public @interface Tweezable {}")//
        .stays();
  }

  @Test public void i() {
    trimmingOf("public @interface hand_made { String[] value(); }final @hand_made({}) String s = \"a\";").stays();
  }
}
