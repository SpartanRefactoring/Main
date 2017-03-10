package il.org.spartan.spartanizer.issues;

import static il.org.spartan.azzert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tippers.*;

/** TODO: RoeyMaor please add a description
 * @author RoeyMaor
 * @author RoeiRaz
 * @since 11-11-16 */
public class Issue0818 {
  static final InfixExpressionConcatentateCompileTime tipper = new InfixExpressionConcatentateCompileTime();
  static final InfixExpression ie01 = into.i("\"a\" + \"b\"");
  static final InfixExpression ie02 = into.i("\"a\" + runTimeValue");
  static final InfixExpression ie03 = into.i("\"a\" * \"b\"");
  static final InfixExpression ie04 = into.i("\"a\" + b + \"c\"");
  static final InfixExpression ie05 = into.i("b + \"a\"");
  static final InfixExpression ie06 = into.i("\"a\" + \"b\" + c");

  @Test @SuppressWarnings("static-method") public void a() {
    azzert.that("ab", is(az.stringLiteral(tipper.replacement(ie01)).getLiteralValue()));
  }

  @Test @SuppressWarnings("static-method") public void b() {
    azzert.isNull(az.stringLiteral(tipper.replacement(ie02)));
  }

  @Test @SuppressWarnings("static-method") public void c() {
    azzert.isNull(az.stringLiteral(tipper.replacement(ie03)));
  }

  @Test @SuppressWarnings("static-method") public void d() {
    assert tipper.description() != null;
  }

  @Test @SuppressWarnings("static-method") public void e() {
    assert tipper.description(null) != null;
  }

  @Test @SuppressWarnings("static-method") public void f() {
    azzert.isNull(az.stringLiteral(tipper.replacement(ie04)));
  }

  @Test @SuppressWarnings("static-method") public void g() {
    azzert.isNull(az.stringLiteral(tipper.replacement(ie05)));
  }

  @Test @SuppressWarnings("static-method") public void h() {
    assert tipper.replacement(ie06) != null;
  }
}
