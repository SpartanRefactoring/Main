package il.org.spartan.automatic;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Auto generated
 * @author Ori Roth
 * @since 2016_12_14 */
// @forget // TODO Yuval: is part of your issue --or
@SuppressWarnings("static-method")
public class Automatic_2016_12_14_01_52_17 {
  /** Test created automatically due to java.lang.NullPointerException thrown
   * while testing some test file. Originated at
   * il.org.spartan.spartanizer.ast.navigate.extract.nextPrefix at line #343. */
  @Test public void nextPrefixTest() {
    trimmingOf("package a;public final class A { public void b(){ try { B.c(d).e().f(g -> h[i++]=j(g)); } catch ( C k) { return null; } }}")
        .doesNotCrash();
  }
  /** Test created automatically due to java.lang.NullPointerException thrown
   * while testing some test file. Originated at
   * il.org.spartan.spartanizer.tippers.LambdaExpressionRemoveRedundantCurlyBraces.prerequisite
   * at line #45. */
  @Test public void prerequisiteTest() {
    trimmingOf("package a;public final class A { public void b(){ try { B.c(d).e().f(g -> h[i++]=j(g)); } catch (C k) { return null; } }}")
        .doesNotCrash();
  }
  /** Test created automatically due to java.lang.NullPointerException thrown
   * while testing some test file. Originated at
   * il.org.spartan.spartanizer.ast.navigate.wizard.redundancies at line
   * #466. */
  @Test public void redundanciesTest() {
    trimmingOf("package a;public final class A { public void b(){ for ( final B c : d) if (c.e(f)) f=c; }}").doesNotCrash();
  }
  /** Test created automatically due to java.lang.NullPointerException thrown
   * while testing some test file. Originated at
   * il.org.spartan.spartanizer.tippers.ExpressionStatementAssertTrueFalse.replacement
   * at line #31. */
  @Test public void replacement3Test() {
    trimmingOf("package a;public final class A { public void b() { int c = d; d = e; return c; }}").doesNotCrash();
  }
  /** Test created automatically due to java.lang.NullPointerException thrown
   * while testing some test file. Originated at
   * il.org.spartan.spartanizer.tippers.SimplifyComparisionOfAdditions.replacement
   * at line #28. */
  @Test public void replacementTest() {
    trimmingOf("package a;public final class A { public void b() { if (c == null)  return; d(); d(); }}").doesNotCrash();
  }
}
