package il.org.spartan.athenizer.zoomers;

import static il.org.spartan.spartanizer.ast.navigate.step.fragments;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.text.edits.TextEditGroup;

import fluent.ly.the;
import il.org.spartan.athenizer.zoom.zoomers.Issue0996;
import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.engine.collect;
import il.org.spartan.spartanizer.java.haz;
import il.org.spartan.spartanizer.tipping.CarefulTipper;
import il.org.spartan.spartanizer.tipping.Tip;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** Test case is {@link Issue0996} Issue #996 convert {@code
 * int a = 3;
 * } to {@code
 * int a;
 * a = 3;
 * } For now the expander do not expand if the declaration has some annotation
 * (so that no warnings would be created)
 * @author tomerdragucki
 * @author Dor Ma'ayan
 * @since 23-12-2016 */
public class DeclarationWithInitializerBloater extends CarefulTipper<VariableDeclarationStatement>//
    implements Category.Bloater {
  private static final long serialVersionUID = 0xF445D29A172AE7CL;

  @Override @SuppressWarnings("unused") public String description(final VariableDeclarationStatement __) {
    return "Split declaration with initialization into two statemenets";
  }
  @Override protected boolean prerequisite(final VariableDeclarationStatement ¢) {
    return !haz.annotation(¢) && ¢.fragments().size() == 1 && ((VariableDeclaration) the.firstOf(fragments(¢))).getInitializer() != null
        && ((VariableDeclaration) the.firstOf(fragments(¢))).getInitializer().getNodeType() != ASTNode.ARRAY_INITIALIZER && (!iz.final¢(¢)
            || collect.usesOf(the.firstOf(fragments(¢)).getName()).in(¢.getParent()).stream().noneMatch(λ -> iz.switchCase(λ.getParent())));
  }
  @Override public Tip tip(final VariableDeclarationStatement ¢) {
    final VariableDeclarationStatement $ = copy.of(¢);
    ((VariableDeclaration) the.firstOf(fragments($))).setInitializer(null);
    final VariableDeclarationFragment f2 = the.firstOf(fragments(¢));
    final Assignment a = subject.pair(copy.of(az.expression(f2.getName())), copy.of(az.expression(f2.getInitializer())))
        .to(Assignment.Operator.ASSIGN);
    return new Tip(description(¢), getClass(), ¢) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final ListRewrite l = r.getListRewrite(¢.getParent(), !SwitchStatement.STATEMENTS_PROPERTY.getNodeClass().isInstance(¢.getParent())
            ? Block.STATEMENTS_PROPERTY : SwitchStatement.STATEMENTS_PROPERTY);
        l.insertAfter(¢.getAST().newExpressionStatement(a), ¢, g);
        l.insertAfter($, ¢, g);
        l.remove(¢, g);
      }
    };
  }
}
