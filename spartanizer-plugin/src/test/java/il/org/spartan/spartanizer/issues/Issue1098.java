package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.junit.Test;

import il.org.spartan.spartanizer.tippers.SwitchSingleCaseToIf;

/** Test case for bug in {@link SwitchSingleCaseToIf}
 * @author YuvalSimon {@code yuvaltechnion@gmail.com}
 * @since 2017-01-20 */
@SuppressWarnings("static-method")
public class Issue1098 {
  @Test public void t1() {
    trimmingOf("int a() {switch (¢ + \"\") {case \"Object\":case \"java.lang.Object\":return true;default:return false;}}").stays();
  }
}
