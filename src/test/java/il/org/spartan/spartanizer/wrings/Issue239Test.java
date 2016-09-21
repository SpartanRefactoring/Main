package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import static il.org.spartan.Utils.*;
import static il.org.spartan.lisp.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.step.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.Inliner.*;
import il.org.spartan.spartanizer.java.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;

/** @author TODO: Matteo???
 * @year 2016 */
@SuppressWarnings("static-method") public class Issue239Test {
  @Test public void a$01() {
    trimmingOf("private void testInteger(final boolean testTransients) {\n" + //
        "final Integer i1 = Integer.valueOf(12344);\n" + //
        "final Integer i2 = Integer.valueOf(12345);\n" + //
        "assertEqualsAndHashCodeContract(i1, i2, testTransients);\n" + //
        "}").gives("private void testInteger(final boolean testTransients) {\n" + //
        "final Integer i1 = Integer.valueOf(12344);\n" + //
             "assertEqualsAndHashCodeContract(i1, Integer.valueOf(12345), testTransients);\n" + //
            "}")
   .gives("private void testInteger(final boolean testTransients) {\n" + //
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
    assert metrics.nodesCount(block) > 10;
    final List<Statement> statements = step.statements(block);
    assert statements != null;
    assert statements.size() == 2;
    ExpressionStatement nextStatement = findFirst.instanceOf(ExpressionStatement.class, block);
    assert lastIn(nextStatement, statements);
    VariableDeclarationFragment f = findFirst.instanceOf(VariableDeclarationFragment.class, block);
    assert f != null;
    final Statement currentStatement = extract.statement(f);
    assert currentStatement != null;
    assert penultimateIn(currentStatement, statements);
    SimpleName name = f.getName();
    assert name != null;
    Expression initializer = f.getInitializer();
    assert initializer != null;
    assert !sideEffects.free(f.getInitializer());
    final List<SimpleName> uses = Collect.usesOf(name).in(nextStatement);
    assert uses.size() == 1;
    SimpleName use = onlyOne(uses);
    assert use != null;
    assert !haz.unknownNumberOfEvaluations(use, nextStatement);
    assert !DeclarationInitializerStatementTerminatingScope.never(name, nextStatement);
    final int addedSize = 0;
    final int removalSaving = $VariableDeclarationFragementAndStatement.removalSaving(f);
    assert removalSaving > addedSize;
  }
}