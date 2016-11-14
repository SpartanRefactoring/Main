package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;

/** Unit test for {@link DeclarationInitializerStatementTerminatingScope} for
 * the case of inlining into the expression of an enhanced for
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue294 {
  private static final String INPUT = "A a=new A();for (A b: g.f(a,true))sum+=b;";
  private static final String OUTPUT = "for(A b: g.f((new A()),true))sum+=b;";
  Statement s = into.s(INPUT);
  EnhancedForStatement forr = findFirst.instanceOf(EnhancedForStatement.class, s);
  BooleanLiteral truex = findFirst.instanceOf(BooleanLiteral.class, s);

  /** Correct way of trimming does not change */
  @Test public void a() {
    trimmingOf(INPUT) //
        .gives(OUTPUT) //
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
    assert !haz.unknownNumberOfEvaluations(truex, s);
  }

  @Test public void f() {
    trimmingOf("for (int a: f(¢)) g(a);") //
        .stays();
  }
}
