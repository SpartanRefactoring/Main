package il.org.spartan.spartanizer.utils.tdd;

import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;

@SuppressWarnings("static-method")
public class Issue774 {
  @Test public void shouldReturnFalseForBooleanLiteral() {
    assert !determineIf.uses(wizard.ast("false"), "false");
  }

  @Test public void shouldReturnFalseForKeyword() {
    assert !determineIf.uses(wizard.ast("class A{}"), "class");
  }

  @Test public void shouldReturnFalseForNull() {
    assert !determineIf.uses(wizard.ast("null"), "null");
  }

  @Test public void shouldReturnFalseForNullNodeObject() {
    assert !determineIf.uses(null, "");
  }

  @Test public void shouldReturnFalseForNullString() {
    assert !determineIf.uses(wizard.ast("t"), null);
  }

  @Test public void shouldReturnFalseForQualifiedNameEnding() {
    assert !determineIf.uses(wizard.ast("a.b.c"), "b.c");
  }

  @Test public void shouldReturnFalseForSubSimpleName() {
    assert !determineIf.uses(wizard.ast("abc.def"), "bc");
  }

  @Test public void shouldReturnFalseIfNameNotExists() {
    assert !determineIf.uses(wizard.ast("x"), "t");
  }

  @Test public void shouldReturnTrueForDescendantSimpleName() {
    assert determineIf.uses(wizard.ast("a.b.c.d"), "c");
  }

  @Test public void shouldReturnTrueForFullyQualifiedName() {
    assert determineIf.uses(wizard.ast("a.b"), "a.b");
  }

  @Test public void shouldReturnTrueForQualifiedNameBeforeDot() {
    assert determineIf.uses(wizard.ast("a.b.c"), "a.b");
  }

  @Test public void shouldReturnTrueForQualifiedNameInsideQualifiedNames() {
    assert determineIf.uses(wizard.ast("a.b.c.d"), "a.b");
  }

  @Test public void shouldReturnTrueForStringLiteral() {
    assert determineIf.uses(wizard.ast("t"), "t");
  }

  @Test public void shouldReturnTrueNameAfterDot() {
    assert determineIf.uses(wizard.ast("a.b"), "b");
  }
}
