package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tippers.*;

/** Unit test for {@link LocalInitializedStatementTerminatingScope} for the case
 * of inlining into the expression of an enhanced for
 * @author Yossi Gil
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0294 {
  final Statement s = into.s("A a=new A();for (A b: g.f(a,true))sum+=b;");
  final EnhancedForStatement forr = findFirst.instanceOf(EnhancedForStatement.class).in(s);
  final BooleanLiteral truex = findFirst.instanceOf(BooleanLiteral.class).in(s);

  /** Correct way of trimming does not change */
  @Test public void a() {
    trimmingOf("A a=new A();for (A b: g.f(a,true))sum+=b;") //
        .gives("for(A b: g.f((new A()),true))sum+=b;") //
        .gives("for(A b: g.f(new A(),true))sum+=b;") //
        .stays();
  }

  @Test public void b() {
    assert iz.expressionOfEnhancedFor(forr.getExpression(), forr);
    assert s != null;
    assert forr != null;
    assert iz.expressionOfEnhancedFor(forr.getExpression(), forr);
  }

  @Test public void c() {
    assert truex != null;
    assert iz.expressionOfEnhancedFor(truex.getParent(), forr);
    assert !iz.expressionOfEnhancedFor(truex, forr);
  }

  @Test public void d() {
    assert iz.expressionOfEnhancedFor(truex.getParent(), forr);
  }

  @Test public void e() {
    assert !Coupling.unknownNumberOfEvaluations(truex, s);
  }

  @Test public void f() {
    trimmingOf("for (int a: f(¢)) g(a);") //
        .stays();
  }
}
