package il.org.spartan.spartanizer.issues;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.utils.*;

/** Failing tests of issue 295 - FragmentInitializerStatementTerminatingScope.
 * DeclarationInitializerStatementTerminatingScope.
 * @author Yossi Gil
 * @since 2016 */
@Ignore
@SuppressWarnings("static-method")
public class issue0411 {
  private static final String INPUT = "A a = new A();for (A b: g.f(a,true))sum+=b;";
  private static final String INPUT1 = "boolean f(){A var=f(1);for(A b: var)if(b.a)return true;return false;}";
  private static final String OUTPUT1 = "boolean f(){for(A b:f(1))if(b.a)return true;return false;}";
  final MethodDeclaration input1 = into.d(INPUT1);
  final EnhancedForStatement forr = findFirst.instanceOf(EnhancedForStatement.class).in(input1);
  NumberLiteral one = findFirst.instanceOf(NumberLiteral.class).in(input1);
  final Statement seriesA$step1 = into.s(INPUT);
  EnhancedForStatement seriesA$step2 = findFirst.instanceOf(EnhancedForStatement.class).in(seriesA$step1);
  final BooleanLiteral seriesA$step3 = findFirst.instanceOf(BooleanLiteral.class).in(seriesA$step1);
  EnhancedForStatement seriesB$step2 = findFirst.instanceOf(EnhancedForStatement.class).in(seriesA$step1);
  final LocalInitializedStatementTerminatingScope tipper = new LocalInitializedStatementTerminatingScope();
  final VariableDeclarationFragment variableDeclarationFragment = findFirst.instanceOf(VariableDeclarationFragment.class).in(input1);

  @Test public void A$c() {
    assert seriesA$step3 != null;
    assert iz.expressionOfEnhancedFor(seriesA$step3.getParent(), seriesA$step1);
    assert !iz.expressionOfEnhancedFor(seriesA$step3, seriesA$step1);
  }

  @Test public void A$d() {
    assert iz.expressionOfEnhancedFor(seriesA$step3.getParent(), seriesA$step1);
  }

  @Test public void B01() {
    trimmingOf(" public static boolean checkVariableDecleration(VariableDeclarationStatement s) { "
        + "List<VariableDeclarationFragment> lst =  fragments(s); for (VariableDeclarationFragment ¢ : lst) "
        + "  if (¢.getInitializer() != null && !sideEffects.free(¢.getInitializer()))     return false; return true; }")
            .gives(" public static boolean checkVariableDecleration(VariableDeclarationStatement s) { "
                + "for (VariableDeclarationFragment ¢ :  fragments(s);) "
                + "  if (¢.getInitializer() != null && !sideEffects.free(¢.getInitializer()))     return false; return true; }")
            .stays();
  }

  @Test public void B02() {
    trimmingOf("void  f(V s) { List<U> lst =  fragments(s); for (U ¢ : lst) "
        + "  if (¢.getInitializer() != null && !sideEffects.free(¢.getInitializer()))     return false; return true; }")
            .gives("void f(V v) { for (U ¢ :  fragments(v))   if (¢.getInitializer() != null && !sideEffects.free(¢.getInitializer())) "
                + "    return false; return true; }")
            .stays();
  }

  @Test public void B03() {
    trimmingOf("void  f(V variableDeclarationFragment) { List<U> lst =  fragments(variableDeclarationFragment); for (U ¢ : lst) "
        + "  if (¢.getInitializer() != null && !sideEffects.free(¢.getInitializer()))     return false; return true; }")
            .gives("void f(V variableDeclarationFragment) { for (U ¢ :  fragments(variableDeclarationFragment);) "
                + "  if (¢.getInitializer() != null && !sideEffects.free(¢.getInitializer()))     return false; return true; }")
            .stays();
  }

  @Test public void B05() {
    trimmingOf("boolean  f(V variableDeclarationFragment) { V x=  fragments(variableDeclarationFragment); for (U ¢ : x) "
        + "  if (¢.getInitializer() != null && !sideEffects.free(¢.getInitializer()))     return false; return true; }")
            .gives("boolean f(V variableDeclarationFragment) { for (U ¢ :  fragments(variableDeclarationFragment);) "
                + "  if (¢.getInitializer() != null && !sideEffects.free(¢.getInitializer()))     return false; return true; }")
            .stays();
  }

  @Test public void B06() {
    trimmingOf("boolean f() { V x= g(variableDeclarationFragment); for (U ¢ : x) "
        + "  if (¢.getInitializer() != null && !sideEffects.free(¢.getInitializer()))     return false; return true; }")
            .gives("boolean f() { for (U ¢ : g(variableDeclarationFragment))"
                + "  if (¢.getInitializer() != null && !sideEffects.free(¢.getInitializer()))     return false; return true; }")
            .stays();
  }

  @Test public void B07() {
    trimmingOf(INPUT1)//
        .gives(OUTPUT1)//
        .stays();
  }

  @Test public void B17() {
    assert tipper.check(variableDeclarationFragment) : fault.dump() + "\n variableDeclarationFragment = " + variableDeclarationFragment + "\n for = "
        + forr + fault.done();
  }

  @Test public void B20() {
    assert variableDeclarationFragment != null;
    azzert.that(tipper.tip(variableDeclarationFragment), iz("a"));
  }

  @Test public void B21() {
    assert tipper.prerequisite(variableDeclarationFragment) : fault.dump() + "\n variableDeclarationFragment = " + variableDeclarationFragment
        + "\n for = " + forr + fault.done();
  }
}
