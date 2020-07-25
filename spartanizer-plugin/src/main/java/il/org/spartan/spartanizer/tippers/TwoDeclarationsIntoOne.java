package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.fragments;
import static il.org.spartan.spartanizer.ast.navigate.step.parent;
import static il.org.spartan.spartanizer.ast.navigate.step.statements;
import static il.org.spartan.spartanizer.ast.navigate.step.type;

import java.util.List;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import fluent.ly.is;
import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.issues.Issue1012;
import il.org.spartan.spartanizer.tipping.GoToNextStatement;
import il.org.spartan.spartanizer.tipping.categories.Category;
import il.org.spartan.utils.Examples;

/** Test case is {@link Issue1012} Issue #1012 Convert: {@code
 * int a = 0;
 * int b = 1;
 * int c = 2;
 * } to: {@code
 * int a = 0, b = 1, c = 2;
 * }
 * @author tomerdragucki {@code tomerd@campus.technion.ac.il}
 * @since 2017-01-13 */
public class TwoDeclarationsIntoOne extends GoToNextStatement<VariableDeclarationStatement>//
    implements Category.CommonFactorOut {
  private static final long serialVersionUID = -0x591B454B69ADD31L;

  @Override protected ASTRewrite go(final ASTRewrite $, final VariableDeclarationStatement s, final Statement nextStatement, final TextEditGroup g) {
    if (!canTip(s, nextStatement))
      return null;
    final VariableDeclarationStatement sc = copy.of(s);
    fragments(az.variableDeclarationStatement(nextStatement)).forEach(λ -> fragments(sc).add(copy.of(λ)));
    $.replace(s, sc, g);
    $.remove(nextStatement, g);
    return $;
  }
  @Override public String description(final VariableDeclarationStatement ¢) {
    return "Unify two variable declarations of type " + ¢.getType() + " into one";
  }
  @Override public Examples examples() {
    return //
    convert("int a; int b; int c; f(a, b, c);") //
        .to("int a, b; int c; f(a, b, c);"). //
        convert("int a, b; int c; f(a, b, c);") //
        .to("int a, b, c; f(a, b, c);"). //
        convert("final int a = 1; final int b = 2; f(a, b);") //
        .to("final int a = 1, b = 2; f(a, b);") //
        .ignores("int a = 1; final int b = 2; f(a, b);") //
    ;
  }
  private static boolean canTip(final VariableDeclarationStatement $, final Statement nextStatement) {
    final Block parent = az.block(parent($));
    return (parent == null || !is.lastIn(nextStatement, statements(parent))) && iz.variableDeclarationStatement(nextStatement)
        && (type(az.variableDeclarationStatement(nextStatement)) + "").equals(type($) + "")
        && az.variableDeclarationStatement(nextStatement).getModifiers() == $.getModifiers()
        && sameAnnotations(extract.annotations($), extract.annotations(az.variableDeclarationStatement(nextStatement)));
  }
  private static boolean sameAnnotations(final List<Annotation> l1, final List<Annotation> l2) {
    return l1.size() == l2.size() && l1.stream().allMatch(λ -> (λ + "").equals(l2.get(l1.indexOf(λ)) + ""));
  }
}
