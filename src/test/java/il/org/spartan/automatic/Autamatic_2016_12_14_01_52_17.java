package il.org.spartan.automatic;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** @author Ori Roth
 * @since 2016_12_14 */
@Ignore
@SuppressWarnings("static-method")
public class Autamatic_2016_12_14_01_52_17 {
  /** Test created automatically due to java.lang.NullPointerException thrown
   * while testing some test file. Originated at
   * il.org.spartan.spartanizer.tippers.LambdaExpressionRemoveRedundantCurlyBraces.prerequisite
   * at line #45. [[SuppressWarningsSpartan]] */
  @Test public void prerequisiteTest() {
    trimmingOf("" + //
        "package a;" + //
        "public final class A {" + //
        "  public void b(){" + //
        "    try {" + //
        "      B.c(d).e().f(g -> h[i++]=j(g));" + //
        "    }" + //
        " catch (    C k) {" + //
        "      return null;" + //
        "    }" + //
        "  }" + //
        "}"//
    ).doesNotCrash();
  }

  /** Test created automatically due to java.lang.NullPointerException thrown
   * while testing some test file. Originated at
   * il.org.spartan.spartanizer.ast.navigate.extract.nextPrefix at line #343.
   * [[SuppressWarningsSpartan]] */
  @Test public void nextPrefixTest() {
    trimmingOf("" + //
        "package a;" + //
        "public final class A {" + //
        "  public void b(){" + //
        "    try {" + //
        "      B.c(d).e().f(g -> h[i++]=j(g));" + //
        "    }" + //
        " catch (    C k) {" + //
        "      return null;" + //
        "    }" + //
        "  }" + //
        "}"//
    ).doesNotCrash();
  }

  /** Test created automatically due to java.lang.NullPointerException thrown
   * while testing some test file. Originated at
   * il.org.spartan.spartanizer.tippers.SimplifyComparisionOfAdditions.replacement
   * at line #28. [[SuppressWarningsSpartan]] */
  @Test public void replacementTest() {
    trimmingOf("" + //
        "package a;" + //
        "" + //
        "public final class A {" + //
        "	public void b() {" + //
        "		if (c == null)" + //
        "			return;" + //
        "		d();" + //
        "		d();" + //
        "	}" + //
        "}"//
    ).doesNotCrash();
  }

  /** Test created automatically due to java.lang.NullPointerException thrown
   * while testing some test file. Originated at
   * il.org.spartan.spartanizer.tippers.ExpressionStatementAssertTrueFalse.replacement
   * at line #31. [[SuppressWarningsSpartan]] */
  @Test public void replacement3Test() {
    trimmingOf("" + //
        "package a;" + //
        "" + //
        "public final class A {" + //
        "	public void b() {" + //
        "		int c = d;" + //
        "		d = e;" + //
        "		return c;" + //
        "	}" + //
        "}"//
    ).doesNotCrash();
  }

  /** Test created automatically due to java.lang.NullPointerException thrown
   * while testing some test file. Originated at
   * il.org.spartan.spartanizer.ast.navigate.wizard.redundancies at line #466.
   * [[SuppressWarningsSpartan]] */
  @Test public void redundanciesTest() {
    trimmingOf("" + //
        "package a;" + //
        "public final class A {" + //
        "  public void b(){" + //
        "    for (    final B c : d)     if (c.e(f))     f=c;" + //
        "  }" + //
        "}"//
    ).doesNotCrash();
  }
}
