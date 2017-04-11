package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

/** This is a unit test for a bug (issue 445) in
 * {@link MethodDeclarationRenameReturnToDollar} of previously failed tests.
 * Related to {@link Issue0445}.
 * @author Yuval Simon
 * @since 2016-12-08 */
@SuppressWarnings("static-method")
public class Issue0910 {
  @Test public void singleVariableDeclarationStatementShouldntTip() {
    trimminKof("x -> {int y;}") //
        .gives("x->{}") //
        .gives("λ->{}") //
        .stays();
  }

  @Test public void singleVariableDeclarationStatementShouldntTip2() {
    trimminKof("x -> {int y = f(x); while (x < f(y)) return x; return y; }") //
        .stays();
  }
}
