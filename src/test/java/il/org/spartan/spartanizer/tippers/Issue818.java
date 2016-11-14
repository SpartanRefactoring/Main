package il.org.spartan.spartanizer.tippers;

import static org.junit.Assert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;

/** @author RoeyMaor
 * @author RoeiRaz
 * @since 11-11-16 */
public class Issue818 {
  static final InfixExpressionConcatentateCompileTime tipper = new InfixExpressionConcatentateCompileTime();
  static final InfixExpression ie01 = into.i("\"a\" + \"b\"");
  static final InfixExpression ie02 = into.i("\"a\" + runTimeValue");
  static final InfixExpression ie03 = into.i("\"a\" * \"b\"");
  static final InfixExpression ie04 = into.i("\"a\" + b + \"c\"");
  static final InfixExpression ie05 = into.i("b + \"a\"");
  static final InfixExpression ie06 = into.i("\"a\" + \"b\" + c");

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

  @SuppressWarnings("static-method") @Test public void c() {
    assertNull(az.stringLiteral(tipper.replacement(ie03)));
  }

  @SuppressWarnings("static-method") @Test public void d() {
    assert tipper.description() != null;
  }

  @SuppressWarnings("static-method") @Test public void e() {
    assert tipper.description(null) != null;
  }

  @SuppressWarnings("static-method") @Test public void f() {
    assertNull(az.stringLiteral(tipper.replacement(ie04)));
  }

  @SuppressWarnings("static-method") @Test public void g() {
    assertNull(az.stringLiteral(tipper.replacement(ie05)));
  }

  @SuppressWarnings("static-method") @Test public void h() {
    assert tipper.replacement(ie06) != null;
  }
}
