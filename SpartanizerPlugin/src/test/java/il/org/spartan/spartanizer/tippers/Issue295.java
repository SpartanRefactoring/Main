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
@Ignore @FixMethodOrder(MethodSorters.NAME_ASCENDING) @SuppressWarnings({ "static-method", "javadoc" }) public class Issue295 {
  private static final String INPUT = "A a = new A();for (A b: g.f(a,true))sum+=b;";
  private static final String INPUT1 = "boolean f(){A var=f(1);for(A b: var)if(b.a)return true;return false;}";
  private static final String OUTPUT = "for (A b: g.f((new A()),true))sum+=b;";
  private static final String OUTPUT1 = "boolean f(){for(A b:f(1))if(b.a)return true;return false;}";
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
    trimmingOf("  public static boolean checkVariableDecleration(VariableDeclarationStatement s) { " + //
        "List<VariableDeclarationFragment> lst = step.fragments(s); " + //
        "for (VariableDeclarationFragment ¢ : lst) " + //
        "  if (¢.getInitializer() != null && !sideEffects.free(¢.getInitializer())) " + //
        "    return false; " + //
        "return true; " + //
        "}").gives("  public static boolean checkVariableDecleration(VariableDeclarationStatement s) { " + //
            "for (VariableDeclarationFragment ¢ : step.fragments(s);) " + //
            "  if (¢.getInitializer() != null && !sideEffects.free(¢.getInitializer())) " + //
            "    return false; " + //
            "return true; " + //
            "}").//
            stays();
  }

  @Test public void B02() {
    trimmingOf("void  f(V s) { " + //
        "List<U> lst = step.fragments(s); " + //
        "for (U ¢ : lst) " + //
        "  if (¢.getInitializer() != null && !sideEffects.free(¢.getInitializer())) " + //
        "    return false; " + //
        "return true; " + //
        "}").gives(" f(U s) { " + //
            "for (U ¢ : step.fragments(s);) " + //
            "  if (¢.getInitializer() != null && !sideEffects.free(¢.getInitializer())) " + //
            "    return false; " + //
            "return true; " + //
            "}").//
            stays();
  }

  @Test public void B03() {
    trimmingOf("void  f(V variableDeclarationFragment) { " + //
        "List<U> lst = step.fragments(variableDeclarationFragment); " + //
        "for (U ¢ : lst) " + //
        "  if (¢.getInitializer() != null && !sideEffects.free(¢.getInitializer())) " + //
        "    return false; " + //
        "return true; " + //
        "}").gives("void f(U variableDeclarationFragment) { " + //
            "for (U ¢ : step.fragments(variableDeclarationFragment);) " + //
            "  if (¢.getInitializer() != null && !sideEffects.free(¢.getInitializer())) " + //
            "    return false; " + //
            "return true; " + //
            "}").//
            stays();
  }

  @Test public void B05() {
    trimmingOf("boolean  f(V variableDeclarationFragment) { " + //
        "V x= step.fragments(variableDeclarationFragment); " + //
        "for (U ¢ : x) " + //
        "  if (¢.getInitializer() != null && !sideEffects.free(¢.getInitializer())) " + //
        "    return false; " + //
        "return true; " + //
        "}").gives("boolean f(U variableDeclarationFragment) { " + //
            "for (U ¢ : step.fragments(variableDeclarationFragment);) " + //
            "  if (¢.getInitializer() != null && !sideEffects.free(¢.getInitializer())) " + //
            "    return false; " + //
            "return true; " + //
            "}").//
            stays();
  }

  @Test public void B06() {
    trimmingOf("boolean f() { " + //
        "V x= g(variableDeclarationFragment); " + //
        "for (U ¢ : x) " + //
        "  if (¢.getInitializer() != null && !sideEffects.free(¢.getInitializer())) " + //
        "    return false; " + //
        "return true; " + //
        "}").gives("boolean f() { " + //
            "for (U ¢ : g(variableDeclarationFragment))" + //
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
    assert variableDeclarationFragment != null;
    azzert.that(variableDeclarationFragment, iz("var=f(1)"));
  }

  @Test public void B17() {
    assert tipper.canTip(variableDeclarationFragment) : LoggingManner.beginDump() + //
        "\n variableDeclarationFragment = " + variableDeclarationFragment + //
        "\n for = " + forr + //
        LoggingManner.endDump();
  }

  @Test public void B18() throws TipperFailure {
    assert tipper.tip(variableDeclarationFragment) != null : LoggingManner.beginDump() + //
        "\n variableDeclarationFragment = " + variableDeclarationFragment + //
        "\n for = " + forr + //
        LoggingManner.endDump();
  }

  @Test public void B19() {
    assert tipper.tip(variableDeclarationFragment, null) != null : LoggingManner.beginDump() + //
        "\n variableDeclarationFragment = " + variableDeclarationFragment + //
        "\n for = " + forr + //
        LoggingManner.endDump();
  }

  @Test public void B20() throws TipperFailure {
    assert variableDeclarationFragment != null;
    azzert.that(tipper.tip(variableDeclarationFragment), iz("a"));
  }

  @Test public void B21() {
    assert tipper.prerequisite(variableDeclarationFragment) : LoggingManner.beginDump() + //
        "\n variableDeclarationFragment = " + variableDeclarationFragment + //
        "\n for = " + forr + //
        LoggingManner.endDump();
  }

  @Test public void B22() {
  }
}