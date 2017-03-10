package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;

/** TODO: Dor Ma'ayan please add a description
 * @author Dor Ma'ayan
 * @since 17-11-2016 */
@Ignore // TODO: Dor M'ayan
@SuppressWarnings("static-method")
public class Issue0499 {
  @Test public void test0() {
    trimmingOf("public S d(final G a) {assert a != null;r(¢ -> Integer.valueOf(a.apply(¢, selection())));return this;}")
        .gives("public S d(final G ¢) {assert ¢ != null;r(¢ -> Integer.valueOf(a.apply(¢, selection())));return this;}")//
        .stays();
  }

  @Test public void test1() {
    trimmingOf("public S d(final G a) {assert a != null;r(x -> Integer.valueOf(a.apply(x, selection())));return this;}").stays();
  }

  @Test public void test2() {
    trimmingOf("public S d(final G a) {assert a != null;r(x -> Integer.valueOf(a.apply(x, selection())));return this;}")
        .gives("public S d(final G ¢) {assert ¢ != null;r(¢ -> Integer.valueOf(a.apply(¢, selection())));return this;}")//
        .stays();
  }
}
