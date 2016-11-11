package il.org.spartan.spartanizer.tippers;

import static org.junit.Assert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;

public class Issue818 {
  static final InfixExpressionConcatentateCompileTime tipper = new InfixExpressionConcatentateCompileTime();
  static final InfixExpression ie01 = into.i("\"a\" + \"b\"");
  static final InfixExpression ie02 = into.i("\"a\" + runTimeValue");

  // This test performs a simple concatenation operation: "a" + "b" => "ab"
  @SuppressWarnings("static-method") @Test public void a() {
    assertEquals(az.stringLiteral(tipper.replacement(ie01)).getLiteralValue(), "ab");
  }
  // This test tries to compile-time concatenate a string literal and a
  // variable.
  // Should fail.
  @SuppressWarnings("static-method") @Test public void b() {
    assertNull(az.stringLiteral(tipper.replacement(ie02)));
  }
}
