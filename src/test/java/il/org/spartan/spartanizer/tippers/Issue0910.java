package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** This is a unit test for a bug (issue 445) in
 * {@link MethodDeclarationRenameReturnToDollar} of previously failed tests.
 * Related to {@link Issue0445}.
 * @author Yuval Simon
 * @since 2016-12-08 */
@SuppressWarnings("static-method")
public class Issue0910 {
  @Test public void singleVariableDeclarationStatementShouldntTip() {
    trimmingOf("x -> {int y;}") //
        .gives("") //
        .stays();
  }

  @Test public void singleVariableDeclarationStatementShouldntTip2() {
    trimmingOf("x -> {int y = f(x); while (x < f(y)) return x; return y; }") //
        .stays();
  }
}
