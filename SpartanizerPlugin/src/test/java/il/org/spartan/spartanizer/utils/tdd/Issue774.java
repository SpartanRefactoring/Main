package il.org.spartan.spartanizer.utils.tdd;

import static org.junit.Assert.*;

import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;

@SuppressWarnings("static-method") public class Issue774 {
  @Test public void shouldReturnFalseForBooleanLiteral() {
    assertFalse(determineIf.uses(wizard.ast("false"), "false"));
  }

  @Test public void shouldReturnTrueForStringLiteral() {
    assertTrue(determineIf.uses(wizard.ast("t"), "t"));
  }

  @Test public void shouldReturnFalseForNull() {
    assertFalse(determineIf.uses(wizard.ast("null"), "null"));
  }

  @Test public void shouldReturnFalseForKeyword() {
    assertFalse(determineIf.uses(wizard.ast("class A{}"), "class"));
  }

  @Test public void shouldReturnFalseIfNameNotExists() {
    assertFalse(determineIf.uses(wizard.ast("x"), "t"));
  }

  @Test public void shouldReturnTrueForFullyQualifiedName() {
    assertTrue(determineIf.uses(wizard.ast("a.b"), "a.b"));
  }

  @Test public void shouldReturnTrueNameAfterDot() {
    assertTrue(determineIf.uses(wizard.ast("a.b"), "b"));
  }
  
  @Test public void shouldReturnTrueForQualifiedNameBeforeDot() {
    assertTrue(determineIf.uses(wizard.ast("a.b.c"), "a.b"));
  }
  
  @Test public void shouldReturnTrueForQualifiedNameInsideQualifiedNames(){
    assertTrue(determineIf.uses(wizard.ast("a.b.c.d"), "a.b"));
  }  

  @Test public void shouldReturnTrueForDescendantSimpleName() {
    assertTrue(determineIf.uses(wizard.ast("a.b.c.d"), "c"));
  }

}
