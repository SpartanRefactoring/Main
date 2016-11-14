package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;
import static il.org.spartan.lisp.*;
import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;

/** @author Matteo Orru'
 * @year 2016 */
@SuppressWarnings("static-method")
public class Issue239 {
  @Test public void a$01() {
    trimmingOf("private void testInteger(final boolean testTransients) {\n" + //
        "final Integer i1 = Integer.valueOf(12344);\n" + //
        "final Integer i2 = Integer.valueOf(12345);\n" + //
        "assertEqualsAndHashCodeContract(i1, i2, testTransients);\n" + //
        "}").gives("private void testInteger(final boolean testTransients) {\n" + //
            "final Integer i1 = Integer.valueOf(12344);\n" + //
            "assertEqualsAndHashCodeContract(i1, Integer.valueOf(12345), testTransients);\n" + //
            "}").gives("private void testInteger(final boolean testTransients) {\n" + //
                "assertEqualsAndHashCodeContract(Integer.valueOf(12344), Integer.valueOf(12345), testTransients);\n" + //
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
                    "final int i1 = Integer.valueOf(1);\n" + //
                    "f1(i1,Integer.valueOf(2));\n" + //
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
    final ExpressionStatement nextStatement = findFirst.instanceOf(ExpressionStatement.class, block);
    assert lastIn(nextStatement, statements);
    final VariableDeclarationFragment f = findFirst.instanceOf(VariableDeclarationFragment.class, block);
    assert f != null;
    final Statement currentStatement = extract.containingStatement(f);
    assert currentStatement != null;
    assert penultimateIn(currentStatement, statements);
    final SimpleName name = f.getName();
    assert name != null;
    final Expression initializer = f.getInitializer();
    assert initializer != null;
    assert haz.sideEffects(f.getInitializer());
    final List<SimpleName> uses = Collect.usesOf(name).in(nextStatement);
    assert uses.size() == 1;
    final SimpleName use = onlyOne(uses);
    assert use != null;
    assert !haz.unknownNumberOfEvaluations(use, nextStatement);
    assert !DeclarationInitializerStatementTerminatingScope.never(name, nextStatement);
    assert $VariableDeclarationFragementAndStatement.removalSaving(f) > 0;
  }
}