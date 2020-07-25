package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.junit.Ignore;
import org.junit.Test;

import il.org.spartan.spartanizer.tippers.InitializationListRemoveComma;

/** This is a unit test for {@link InitializationListRemoveComma} The tests
 * issue74a-d are taken from {@link Version290} and {@link Version230}
 * @author Yuval Simon
 * @since 2016-12-09 */
@Ignore
@SuppressWarnings("static-method")
public class Issue0074 {
  // TODO Yuval Simon unignore a test for this tipper in {@link Version 230}
  @Test public void issue74a() {
    trimmingOf("int[] a = new int[] {,}")//
        .gives("int[] a = new int[] {}");
  }
  @Test public void issue74b() {
    trimmingOf("int[] a = new int[] {2,3,}")//
        .gives("int[] a = new int[] {2,3}");
  }
  @Test public void issue74c() {
    trimmingOf("a = new int[]{2,3,}")//
        .gives("a = new int[] {2,3}");
  }
  @Test public void issue74d() {
    trimmingOf("int[] a = new int[] {2,3};")//
        .gives("");
  }
}
