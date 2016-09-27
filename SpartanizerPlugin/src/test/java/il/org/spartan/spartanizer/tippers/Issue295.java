package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** Unit test for {@link DeclarationInitializerStatementTerminatingScope} for
 * the case of inlining into the expression of an enhanced for
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) @SuppressWarnings({ "static-method", "javadoc" }) public class Issue295 {
  private static final String INPUT = "A a = new A();for (A b: g.f(a,true))sum+=b;";
  private static final String OUTPUT = "for (A b: g.f((new A()),true))sum+=b;";
  private static final String INPUT1 = "boolean f(){A var=f(1);for(A b: var)if(b.a)return true;return false;}";
  private static final String OUTPUT1 = "boolean f(){for(A b:f(1))if(b.a)return true;return false;}";
  Statement seriesA$step1 = into.s(INPUT);
  EnhancedForStatement seriesA$step2 = findFirst.instanceOf(EnhancedForStatement.class, seriesA$step1);
  BooleanLiteral seriesA$step3 = findFirst.instanceOf(BooleanLiteral.class, seriesA$step1);
  MethodDeclaration input1 = into.d(INPUT1);
  EnhancedForStatement seriesB$step2 = findFirst.instanceOf(EnhancedForStatement.class, seriesA$step1);
  EnhancedForStatement forr = findFirst.instanceOf(EnhancedForStatement.class, input1);
  NumberLiteral one = findFirst.instanceOf(NumberLiteral.class, input1);

  /** Correct way of trimming does not change */
  @Test public void A$a() {
    trimmingOf(INPUT) //
        .gives(OUTPUT) //
        .stays();
  }

  @Test public void A$b() {
    assert seriesA$step1 != null;
    assert seriesA$step2 != null;
    assert iz.expressionOfEnhancedFor(seriesA$step2.getExpression(), seriesA$step1);
    assert iz.expressionOfEnhancedFor(seriesA$step2.getExpression(), seriesA$step1);
  }

  @Test public void A$c() {
    assert seriesA$step3 != null;
    assert iz.expressionOfEnhancedFor(seriesA$step3.getParent(), seriesA$step1);
    assert !iz.expressionOfEnhancedFor(seriesA$step3, seriesA$step1);
  }

  @Test public void A$d() {
    assert iz.expressionOfEnhancedFor(seriesA$step3.getParent(), seriesA$step1);
  }

  @Test public void A$e() {
    assert !haz.unknownNumberOfEvaluations(seriesA$step3, seriesA$step1);
  }

  @Test public void B01() {
    trimmingOf("  public static boolean checkVariableDecleration(VariableDeclarationStatement example1step1) { " + //
        "List<VariableDeclarationFragment> lst = step.fragments(example1step1); " + //
        "for (VariableDeclarationFragment ¢ : lst) " + //
        "  if (¢.getInitializer() != null && !sideEffects.free(¢.getInitializer())) " + //
        "    return false; " + //
        "return true; " + //
        "}").gives("  public static boolean checkVariableDecleration(VariableDeclarationStatement example1step1) { " + //
            "for (VariableDeclarationFragment ¢ : step.fragments(example1step1);) " + //
            "  if (¢.getInitializer() != null && !sideEffects.free(¢.getInitializer())) " + //
            "    return false; " + //
            "return true; " + //
            "}").//
            stays();
  }

  @Test public void B02() {
    trimmingOf("void  f(V example1step1) { " + //
        "List<U> lst = step.fragments(example1step1); " + //
        "for (U ¢ : lst) " + //
        "  if (¢.getInitializer() != null && !sideEffects.free(¢.getInitializer())) " + //
        "    return false; " + //
        "return true; " + //
        "}").gives(" f(U example1step1) { " + //
            "for (U ¢ : step.fragments(example1step1);) " + //
            "  if (¢.getInitializer() != null && !sideEffects.free(¢.getInitializer())) " + //
            "    return false; " + //
            "return true; " + //
            "}").//
            stays();
  }

  @Test public void B03() {
    trimmingOf("void  f(V v) { " + //
        "List<U> lst = step.fragments(v); " + //
        "for (U ¢ : lst) " + //
        "  if (¢.getInitializer() != null && !sideEffects.free(¢.getInitializer())) " + //
        "    return false; " + //
        "return true; " + //
        "}").gives("void f(U v) { " + //
            "for (U ¢ : step.fragments(v);) " + //
            "  if (¢.getInitializer() != null && !sideEffects.free(¢.getInitializer())) " + //
            "    return false; " + //
            "return true; " + //
            "}").//
            stays();
  }

  @Test public void B05() {
    trimmingOf("boolean  f(V v) { " + //
        "V x= step.fragments(v); " + //
        "for (U ¢ : x) " + //
        "  if (¢.getInitializer() != null && !sideEffects.free(¢.getInitializer())) " + //
        "    return false; " + //
        "return true; " + //
        "}").gives("boolean f(U v) { " + //
            "for (U ¢ : step.fragments(v);) " + //
            "  if (¢.getInitializer() != null && !sideEffects.free(¢.getInitializer())) " + //
            "    return false; " + //
            "return true; " + //
            "}").//
            stays();
  }

  @Test public void B06() {
    trimmingOf("boolean f() { " + //
        "V x= g(v); " + //
        "for (U ¢ : x) " + //
        "  if (¢.getInitializer() != null && !sideEffects.free(¢.getInitializer())) " + //
        "    return false; " + //
        "return true; " + //
        "}").gives("boolean f() { " + //
            "for (U ¢ : g(v))" + //
            "  if (¢.getInitializer() != null && !sideEffects.free(¢.getInitializer())) " + //
            "    return false; " + //
            "return true; " + //
            "}").stays();
  }

  @Test public void B07() {
    trimmingOf(INPUT1) //
        .gives(OUTPUT1)//
        .stays();
  }

  @Test public void B08() {
    assert one != null : LoggingManner.beginDump() + //
        "\n input1 = " + input1 + //
        "\n AST = " + input1.getAST() + //
        LoggingManner.endDump();
  }

  @Test public void B09() {
    assert forr != null : LoggingManner.beginDump() + //
        "\n input1 = " + input1 + //
        "\n AST = " + input1.getAST() + //
        LoggingManner.endDump();
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
    final Expression es = step.expression(forr);
    assert es != null;
    assert !haz.unknownNumberOfEvaluations(es, forr);
  }

  @Test public void B16() {
    final VariableDeclarationFragment v = findFirst.instanceOf(VariableDeclarationFragment.class, input1);
    assert v != null;
    azzert.that(v, iz("var=f(1)"));
  }
  @Test public void B21() {
    final DeclarationInitializerStatementTerminatingScope t = new DeclarationInitializerStatementTerminatingScope();
    final VariableDeclarationFragment v = findFirst.instanceOf(VariableDeclarationFragment.class, input1);
    assert t.prerequisite(v) : LoggingManner.beginDump() + //
        "\n v = " + v + //
        "\n forr = " + forr + //
        LoggingManner.endDump();
  }

  @Test public void B17() {
    final DeclarationInitializerStatementTerminatingScope t = new DeclarationInitializerStatementTerminatingScope();
    final VariableDeclarationFragment v = findFirst.instanceOf(VariableDeclarationFragment.class, input1);
    assert t.canTip(v) : LoggingManner.beginDump() + //
        "\n v = " + v + //
        "\n forr = " + forr + //
        LoggingManner.endDump();
  }

  @Test public void B18() throws TipperFailure {
    final DeclarationInitializerStatementTerminatingScope t = new DeclarationInitializerStatementTerminatingScope();
    final VariableDeclarationFragment v = findFirst.instanceOf(VariableDeclarationFragment.class, input1);
    assert t.tip(v) != null : LoggingManner.beginDump() + //
        "\n v = " + v + //
        "\n forr = " + forr + //
        LoggingManner.endDump();
  }

  @Test public void B19() {
    final DeclarationInitializerStatementTerminatingScope t = new DeclarationInitializerStatementTerminatingScope();
    final VariableDeclarationFragment v = findFirst.instanceOf(VariableDeclarationFragment.class, input1);
    assert t.tip(v, null) != null : LoggingManner.beginDump() + //
        "\n v = " + v + //
        "\n forr = " + forr + //
        LoggingManner.endDump();
  }

  @Test public void B20() throws TipperFailure {
    final DeclarationInitializerStatementTerminatingScope t = new DeclarationInitializerStatementTerminatingScope();
    final VariableDeclarationFragment v = findFirst.instanceOf(VariableDeclarationFragment.class, input1);
    assert v != null;
    azzert.that(t.tip(v), iz("a"));
  }
}