package il.org.spartan.spartanizer.issues;

import static il.org.spartan.Utils.*;
import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tippers.*;
import nano.ly.*;

/** *
 * @author Matteo Orru'
 * @since Jan 6, 2017 */
@SuppressWarnings("static-method")
public class Issue0239 {
  @Test public void a$01() {
    trimmingOf("private void testInteger(final boolean testTransients) {\n" + //
        "final Integer i1 = Integer.valueOf(12344);\n" + //
        "final Integer i2 = Integer.valueOf(12345);\n" + //
        "wizard.assertEqualsAndHashCodeContract(i1, i2, testTransients);\n" + //
        "}")//
            .gives("private void testInteger(final boolean testTransients) {\n" + //
                "final Integer i1 = Integer.valueOf(12344)\n" + //
                ", i2 = Integer.valueOf(12345);\n" + //
                "wizard.assertEqualsAndHashCodeContract(i1, i2, testTransients);\n" + //
                "}")//
            .gives("private void testInteger(final boolean testTransients) {\n" + //
                "final Integer i2 = Integer.valueOf(12345);\n" + //
                "wizard.assertEqualsAndHashCodeContract(Integer.valueOf(12344), i2, testTransients);\n" + //
                "}")
            .gives("private void testInteger(final boolean testTransients) {\n" + //
                "wizard.assertEqualsAndHashCodeContract(Integer.valueOf(12344), Integer.valueOf(12345), testTransients);\n" + //
                "}")
            .stays();
  }

  @Test public void a$02() {
    trimmingOf(//
        "int f() {\n" + //
            "  final int i1 = Integer.valueOf(1);\n" + //
            "  final int i2 = Integer.valueOf(2);\n" + //
            "  f1(i1,i2);\n" + //
            "}"). //
                gives("int f() {\n" + //
                    "  final int i1 = Integer.valueOf(1)\n" + //
                    "  , i2 = Integer.valueOf(2);\n" + //
                    "  f1(i1,i2);\n" + //
                    "}")//
                .gives("int f() {\n" + //
                    "final int i2 = Integer.valueOf(2);\n" + //
                    "f1(Integer.valueOf(1),i2);\n" + //
                    "}")//
                .gives("int f() {\n" + //
                    "f1(Integer.valueOf(1),Integer.valueOf(2));\n" + //
                    "}")
                . //
                stays();
  }

  @Test public void a$03() {
    trimmingOf(//
        "int f() {\n" + //
            "  final int i2 = Integer.valueOf(2);\n" + //
            "  f1(i1,i2);\n" + //
            "}"). //
                gives("int f() {\n" + //
                    "f1(i1,Integer.valueOf(2));\n" + //
                    "}")//
                . //
                stays();
  }

  @Test public void a$04() {
    final Block block = az.block(into.s( //
        "  final int i2 = Integer.valueOf(2);\n" + //
            "  f1(i1,i2);\n"//
    )); //
    assert block != null;
    assert count.nodes(block) > 10;
    final List<Statement> statements = statements(block);
    assert statements != null;
    assert statements.size() == 2;
    final ExpressionStatement nextStatement = findFirst.instanceOf(ExpressionStatement.class).in(block);
    assert lastIn(nextStatement, statements);
    final VariableDeclarationFragment f = findFirst.instanceOf(VariableDeclarationFragment.class).in(block);
    assert f != null;
    final Statement currentStatement = containing.statement(f);
    assert currentStatement != null;
    assert penultimateIn(currentStatement, statements);
    final SimpleName name = f.getName();
    assert name != null;
    assert f.getInitializer() != null;
    assert !sideEffects.free(f.getInitializer());
    final List<SimpleName> uses = collect.usesOf(name).in(nextStatement);
    assert uses.size() == 1;
    final SimpleName use = the.onlyOneOf(uses);
    assert use != null;
    assert !Coupling.unknownNumberOfEvaluations(use, nextStatement);
    assert !Inliner.never(name, nextStatement);
    assert $FragmentAndStatement.removalSaving(f) > 0;
  }
}
