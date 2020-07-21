package il.org.spartan.spartanizer.issues;

import static fluent.ly.azzert.*;
import static il.org.spartan.spartanizer.ast.navigate.GuessedContext.*;

import org.junit.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.nominal.*;

/** Test class for {@link GuessedContext} .
 * @since 2016 */
@Ignore
@SuppressWarnings("static-method")
public class Issue0410 {
  @Test public void dealWithBothKindsOfComment() {
    similar("if (b) {\n", "if (b) {;} { throw new Exception(); }");
  }
  @Test public void findVariable() {
    azzert.that(find("i"), is(EXPRESSION_LOOK_ALIKE));
  }
  @Test public void removeCommentsTest() {
    similar(Trivia.removeComments("if (b) {\n"), "if (b) {} else { throw new Exception(); }");
  }
  private void similar(final String s1, final String s2) {
    azzert.that(Trivia.essence(s2), is(Trivia.essence(s1)));
  }
}
