package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.cmdline.GuessedContext.*;

import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.cmdline.*;

/** Test class for {@link GuessedContext} .
 * @since 2016 */
@Ignore
@SuppressWarnings("static-method")
public class issue410 {
  @Test public void dealWithBothKindsOfComment() {
    similar("if (b) {\n", "if (b) {;} { throw new Exception(); }");
  }

  @Test public void findVariable() {
    azzert.that(find("i"), is(EXPRESSION_LOOK_ALIKE));
  }

  @Test public void removeCommentsTest() {
    similar(wizard.removeComments2("if (b) {\n"), "if (b) {} else { throw new Exception(); }");
  }

  private void similar(final String s1, final String s2) {
    azzert.that(wizard.essence(s2), is(wizard.essence(s1)));
  }
}
