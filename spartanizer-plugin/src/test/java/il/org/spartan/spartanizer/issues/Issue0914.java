package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.junit.Test;

/** This is a unit test for a bug (issue 445) in {@link WhileNextReturnToWhile}
 * of previously failed tests. Related to {@link Issue0311} and
 * {@link Issue0281}
 * @author Yuval Simon
 * @since 2016-12-08 */
@SuppressWarnings("static-method")
public class Issue0914 {
  @Test public void actualBug() {
    trimmingOf(" private static MethodDeclaration findMethodAncestor(final ASTNode ¢) {ASTNode $ = ¢;"
        + "while (!iz.methodDeclaration($) && $ != null)$ = $.getParent();return az.methodDeclaration($);}")//
            .stays();
  }
  @Test public void challenge_while_d() {
    trimmingOf("static X f(final S ¢) {X $ = ¢.elze();" + //
        "while ($ instanceof S)$ = ((S) $).elze();return $;}")//
            .stays();
  }
  @Test public void initializers_while_3() {
    trimmingOf("public boolean check(int i) {int p = i, a = 0; f(++a);while(p <10) ++p;return false;}")//
        .stays();
  }
  @Test public void initializers_while_4() {
    trimmingOf("public boolean check(ASTNode i) {ASTNode p = i; int a = 5; f(++a);while(p <10) p = p.getParent();return false;}").stays();
  }
  @Test public void t05() {
    trimmingOf("static X f(final S ¢) {X $ = ¢.elze();while ($ instanceof S)$ = ((S) $).elze();return $;}")//
        .stays();
  }
  @Test public void test0() {
    trimmingOf("static X f(final S ¢) {X $ = ¢.elze();while ($ instanceof S)$ = ((S) $).elze();return $;}")//
        .stays();
  }
}
