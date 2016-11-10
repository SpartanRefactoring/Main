package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.utils.*;

/** Unit test for {@link DeclarationInitializerStatementTerminatingScope} for
 * the case of inlining into the expression of an enhanced for
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) @SuppressWarnings({ "static-method", "javadoc" }) public class Issue295 {
  private static final String INPUT = "A a = new A();for (A b: g.f(a,true))sum+=b;";
  private static final String INPUT1 = "boolean f(){A var=f(1);for(A b: var)if(b.a)return true;return false;}";
  private static final String OUTPUT = "for (A b: g.f((new A()),true))sum+=b;";
  MethodDeclaration input1 = into.d(INPUT1);
  EnhancedForStatement forr = findFirst.instanceOf(EnhancedForStatement.class, input1);
  NumberLiteral one = findFirst.instanceOf(NumberLiteral.class, input1);
  Statement seriesA$step1 = into.s(INPUT);
  EnhancedForStatement seriesA$step2 = findFirst.instanceOf(EnhancedForStatement.class, seriesA$step1);
  BooleanLiteral seriesA$step3 = findFirst.instanceOf(BooleanLiteral.class, seriesA$step1);
  EnhancedForStatement seriesB$step2 = findFirst.instanceOf(EnhancedForStatement.class, seriesA$step1);
  final DeclarationInitializerStatementTerminatingScope tipper = new DeclarationInitializerStatementTerminatingScope();
  final VariableDeclarationFragment variableDeclarationFragment = findFirst.instanceOf(VariableDeclarationFragment.class, input1);

  /** Correct way of trimming does not change */
  @Test public void A$a() {
    trimmingOf(INPUT) //
        .gives(OUTPUT) //
        .stays();
  }
  @Test public void A$b() {
    assert seriesA$step1 != null;
    assert seriesA$step2 != null;
    assert iz.expressionOfEnhancedFor(seriesA$step2.getExpression(), seriesA$step2);
    assert !iz.expressionOfEnhancedFor(seriesA$step2.getExpression(), seriesA$step1);
  }
  @Test public void A$e() {
    assert !haz.unknownNumberOfEvaluations(seriesA$step3, seriesA$step1);
  }
  @Test public void B08() {
    assert one != null : fault.dump() + //
        "\n input1 = " + input1 + //
        "\n AST = " + input1.getAST() + //
        fault.done();
  }
  @Test public void B09() {
    assert forr != null : fault.dump() + //
        "\n input1 = " + input1 + //
        "\n AST = " + input1.getAST() + //
        fault.done();
  }
  @Test public void B10() {
    assert !haz.unknownNumberOfEvaluations(one, forr);
  }
  @Test public void B11() {
    final ASTNode parent = one.getParent();
    assert parent != null;
    assert !haz.unknownNumberOfEvaluations(parent, forr);
  }
  @Test public void B12() {
    final ASTNode parent = one.getParent().getParent();
    assert parent != null;
    assert !haz.unknownNumberOfEvaluations(parent, forr);
  }
  @Test public void B13() {
    final ASTNode parent = one.getParent().getParent().getParent();
    assert parent != null;
    assert !haz.unknownNumberOfEvaluations(parent, forr);
  }
  @Test public void B14() {
    azzert.that(step.expression(forr), iz("var"));
  }
  @Test public void B15() {
    final Expression es = expression(forr);
    assert es != null;
    assert !haz.unknownNumberOfEvaluations(es, forr);
  }
  @Test public void B16() {
    assert variableDeclarationFragment != null;
    azzert.that(variableDeclarationFragment, iz("var=f(1)"));
  }
  @Test public void B18() {
    assert tipper.tip(variableDeclarationFragment) != null : fault.dump() + //
        "\n variableDeclarationFragment = " + variableDeclarationFragment + //
        "\n for = " + forr + //
        fault.done();
  }
  @Test public void B19() {
    assert tipper.tip(variableDeclarationFragment, null) != null : fault.dump() + //
        "\n variableDeclarationFragment = " + variableDeclarationFragment + //
        "\n for = " + forr + //
        fault.done();
  }
}