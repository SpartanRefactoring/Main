package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for {@link $BodyDeclarationModifiersSort}
 * @author Alex Kopzon
 * @since 2016-09 */
@Ignore @FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue230 {
  @Test public void a() {
    trimmingOf("protected public final class A{volatile static int a;}").gives("public protected final class A{volatile static int a;}")
        .gives("public protected final class A{static volatile int a;}").stays();
  }
  @Test public void a1() {
    trimmingOf("class A{volatile volatile int a;}").gives("class A{volatile int a;}").stays();
  }
  @Test public void a11() {
    trimmingOf("class A{@UserDefined1 @UserDefined1 int a;}").stays();
  }
  @Test public void a111() {
    trimmingOf("class A{@Deprecated @Deprecated int a;}").gives("class A{@Deprecated int a;}").stays();
  }
  @Test public void a2() {
    trimmingOf("@Nullable private T value = null;")//
        .stays();
  }
  @Test public void a3() {
    trimmingOf("class A{volatile @Override static static @Deprecated int f() {}}").gives("class A{@Override @Deprecated static volatile int f() {}}")
        .stays();
  }
  @Test public void a31() {
    trimmingOf("class A{@Override static static @Deprecated int f() {}}").gives("class A{@Override @Deprecated static int f() {}}").stays();
  }
  @Test public void a32() {
    trimmingOf("class A{@Deprecated @Override static static int f() {}}").gives("class A{@Override @Deprecated static int f() {}}").stays();
  }
  @Test public void a33() {
    trimmingOf("class A{@UserDefined @Override static static int f() {}}").gives("class A{@Override @UserDefined static int f() {}}").stays();
  }
  @Test public void a34() {
    trimmingOf("class A{@UserDefined1 @UserDefined2 @Override static static int f() {}}")
        .gives("class A{@Override @UserDefined1 @UserDefined2 static int f() {}}").stays();
  }
  @Test public void a35() {
    trimmingOf("class A{@UserDefined1 @UserDefined2 @Override int f() {}}").gives("class A{@Override @UserDefined1 @UserDefined2 int f() {}}")
        .stays();
  }
  @Test public void a36() {
    trimmingOf("class A{@UserDefined1 @UserDefined2 int f() {}}").stays();
  }
  @Test public void a37() {
    trimmingOf("class A{@UserDefined1 @UserDefined2 @Override @UserDefined1 int f() {}}")
        .gives("class A{@Override @UserDefined1 @UserDefined2 @UserDefined1 int f() {}}").stays();
  }
  @Test public void b() {
    trimmingOf("protected public final class A{volatile static int a;}").gives("public protected final class A{volatile static int a;}")
        .gives("public protected final class A{static volatile int a;}").stays();
  }
  @Test public void c() {
    trimmingOf("protected public final @Deprecated class A{volatile static int a;}")
        .gives("@Deprecated public protected final class A{volatile static int a;}")
        .gives("@Deprecated public protected final class A{static volatile int a;}").stays();
  }
  @Test public void d() {
    trimmingOf("protected public final @Deprecated class A{volatile static @SuppressWarnings(\"deprecation\") int a;}")
        .gives("@Deprecated public protected final class A{volatile static @SuppressWarnings(\"deprecation\") int a;}")
        .gives("@Deprecated public protected final class A{@SuppressWarnings(\"deprecation\") static volatile int a;}").stays();
  }
  @Test public void e() {
    trimmingOf("class A{volatile @Deprecated static @Override int f() {}}").gives("class A{@Override @Deprecated static volatile int f() {}}")
        .stays();
  }
  @Test public void f() {
    trimmingOf("class A{volatile @Override static @Deprecated int f() {}}").gives("class A{@Override @Deprecated static volatile int f() {}}")
        .stays();
  }
  @Test public void g() {
    trimmingOf(
        "public @interface Prio {" + "public enum Priority { LOW, MEDIUM, HIGH }" + "String value();" + "Priority priority() default Priority.MEDIUM;"
            + "}" + "class A {" + "static @Override @Prio(priority=HIGH, value=\"Alex\") public @Deprecated void func() {}}")
                .gives("public @interface Prio {" + "public enum Priority { LOW, MEDIUM, HIGH }" + "String value();"
                    + "Priority priority() default Priority.MEDIUM;" + "}" + "class A {"
                    + "@Override @Deprecated @Prio(priority=HIGH, value=\"Alex\") public static void func() {}}")
                .stays();
  }
  @Test public void h() {
    trimmingOf("@Retention(RetentionPolicy.RUNTIME)" + "@Target({ElementType.METHOD})" + "public @interface Tweezable {}").stays();
  }
  @Test public void i() {
    trimmingOf("public @interface hand_made { String[] value(); }" + "final @hand_made({}) String s = \"a\";")
        .gives("public @interface hand_made { String[] value(); }" + "@hand_made({}) final String s = \"a\";").stays();
  }
  @Test public void j() {
    trimmingOf("@Target({ElementType.METHOD})" + "@Inherited " + "public @interface Prio {" + "public enum Priority { LOW, MEDIUM, HIGH }"
        + "String value();" + "Priority priority() default Priority.MEDIUM;" + "}"
        + "public final static class A {public @Prio(priority=HIGH, value=\"Alex\")"
        + "void func() {public final static class B {public final static @Prio(priority=HIGH, value=\"Alex\")"
        + "public final static void foo() {public final static class C {}}}}}")
            .gives("@Target({ElementType.METHOD})" + "@Inherited " + "public @interface Prio {" + "public enum Priority { LOW, MEDIUM, HIGH }"
                + "String value();" + "Priority priority() default Priority.MEDIUM;" + "}"
                + "public static final class A {public @Prio(priority=HIGH, value=\"Alex\")"
                + "void func() {public final static class B {public final static @Prio(priority=HIGH, value=\"Alex\")"
                + "public final static void foo() {public final static class C {}}}}}")
            .gives("@Target({ElementType.METHOD})" + "@Inherited " + "public @interface Prio {" + "public enum Priority { LOW, MEDIUM, HIGH }"
                + "String value();" + "Priority priority() default Priority.MEDIUM;" + "}"
                + "public static final class A {@Prio(priority=HIGH, value=\"Alex\") public "
                + "void func() {public final static class B {public final static @Prio(priority=HIGH, value=\"Alex\")"
                + "public final static void foo() {public final static class C {}}}}}")
            .gives("@Target({ElementType.METHOD})" + "@Inherited " + "public @interface Prio {" + "public enum Priority { LOW, MEDIUM, HIGH }"
                + "String value();" + "Priority priority() default Priority.MEDIUM;" + "}"
                + "public static final class A {@Prio(priority=HIGH, value=\"Alex\") public "
                + "void func() {public static final class B {public final static @Prio(priority=HIGH, value=\"Alex\")"
                + "public final static void foo() {public final static class C {}}}}}")
            .gives("@Target({ElementType.METHOD})" + "@Inherited " + "public @interface Prio {" + "public enum Priority { LOW, MEDIUM, HIGH }"
                + "String value();" + "Priority priority() default Priority.MEDIUM;" + "}"
                + "public static final class A {@Prio(priority=HIGH, value=\"Alex\") public "
                + "void func() {public static final class B {public static @Prio(priority=HIGH, value=\"Alex\")"
                + "public static void foo() {public final static class C {}}}}}")
            .gives("@Target({ElementType.METHOD})" + "@Inherited " + "public @interface Prio {" + "public enum Priority { LOW, MEDIUM, HIGH }"
                + "String value();" + "Priority priority() default Priority.MEDIUM;" + "}"
                + "public static final class A {@Prio(priority=HIGH, value=\"Alex\") public "
                + "void func() {public static final class B {@Prio(priority=HIGH, value=\"Alex\")"
                + "public static void foo() {public final static class C {}}}}}")
            .gives("@Target({ElementType.METHOD})" + "@Inherited " + "public @interface Prio {" + "public enum Priority { LOW, MEDIUM, HIGH }"
                + "String value();" + "Priority priority() default Priority.MEDIUM;" + "}"
                + "public static final class A {@Prio(priority=HIGH, value=\"Alex\") public "
                + "void func() {public static final class B {@Prio(priority=HIGH, value=\"Alex\")"
                + "public static void foo() {public static final class C {}}}}}")
            .stays();
  }
}
