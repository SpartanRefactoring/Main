package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** This is a unit test for a bug (issue 445) in
 * {@link MethodDeclarationRenameReturnToDollar} of previously failed tests.
 * Related to {@link Issue445}.
 * @author Yuval Simon
 * @since 2016-12-08 */
@Ignore
@SuppressWarnings("static-method")
public class Issue910 {
  @Test public void singleVariableDeclarationStatementShouldntTip() {
    trimmingOf("x -> {int y;}") //
        .gives("x -> {}").stays();
  }
}
