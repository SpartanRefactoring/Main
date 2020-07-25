/* TODO Yossi Gil Document Class
 *
 * @author Yossi Gil
 *
 * @since Jan 15, 2017 */
package il.org.spartan.spartanizer.utils.tdd;

import org.junit.Test;

import il.org.spartan.spartanizer.ast.factory.make;

@SuppressWarnings("static-method")
public class Issue0774 {
  @Test public void shouldReturnFalseForBooleanLiteral() {
    assert !determineIf.uses(make.ast("false"), "false");
  }
  @Test public void shouldReturnFalseForKeyword() {
    assert !determineIf.uses(make.ast("class A{}"), "class");
  }
  @Test public void shouldReturnFalseForNull() {
    assert !determineIf.uses(make.ast("null"), "null");
  }
  @Test public void shouldReturnFalseForNullNodeObject() {
    assert !determineIf.uses(null, "");
  }
  @Test public void shouldReturnFalseForNullString() {
    assert !determineIf.uses(make.ast("t"), null);
  }
  @Test public void shouldReturnFalseForQualifiedNameEnding() {
    assert !determineIf.uses(make.ast("a.b.c"), "b.c");
  }
  @Test public void shouldReturnFalseForSubSimpleName() {
    assert !determineIf.uses(make.ast("abc.def"), "bc");
  }
  @Test public void shouldReturnFalseIfNameNotExists() {
    assert !determineIf.uses(make.ast("x"), "t");
  }
  @Test public void shouldReturnTrueForDescendantSimpleName() {
    assert determineIf.uses(make.ast("a.b.c.d"), "c");
  }
  @Test public void shouldReturnTrueForFullyQualifiedName() {
    assert determineIf.uses(make.ast("a.b"), "a.b");
  }
  @Test public void shouldReturnTrueForQualifiedNameBeforeDot() {
    assert determineIf.uses(make.ast("a.b.c"), "a.b");
  }
  @Test public void shouldReturnTrueForQualifiedNameInsideQualifiedNames() {
    assert determineIf.uses(make.ast("a.b.c.d"), "a.b");
  }
  @Test public void shouldReturnTrueForStringLiteral() {
    assert determineIf.uses(make.ast("t"), "t");
  }
  @Test public void shouldReturnTrueNameAfterDot() {
    assert determineIf.uses(make.ast("a.b"), "b");
  }
}
