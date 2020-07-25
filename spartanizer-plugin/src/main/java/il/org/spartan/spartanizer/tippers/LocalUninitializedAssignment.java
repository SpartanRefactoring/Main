package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.from;
import static il.org.spartan.spartanizer.ast.navigate.step.to;
import static org.eclipse.jdt.core.dom.Assignment.Operator.ASSIGN;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.navigate.wizard;
import il.org.spartan.spartanizer.tipping.categories.Category;
import il.org.spartan.utils.Examples;

/** convert {@code
 * int a;
 * a = 3;
 * } into {@code
 * int a = 3;
 * }
 * @author Yossi Gil
 * @since 2015-08-07 */
public final class LocalUninitializedAssignment extends LocalUninitialized //
    implements Category.Collapse {
  private static final long serialVersionUID = 0xCE4CF4E3910F992L;

  private VariableDeclarationStatement makeVariableDeclarationFragement() {
    final VariableDeclarationFragment f = copy.of(current);
    f.setInitializer(copy.of(from));
    final VariableDeclarationStatement $ = current.getAST().newVariableDeclarationStatement(f);
    $.setType(copy.of(declaration.getType()));
    return $;
  }

  private Assignment assignment;
  private Expression to;
  private Expression from;

  public LocalUninitializedAssignment() {
    needs("Next statement is an assignment", () -> assignment = extract.assignment(nextStatement));
    property("To", () -> to = to(assignment));
    property("From", () -> from = from(assignment));
    andAlso("It is a non-update assignment ", () -> assignment.getOperator() == ASSIGN);
    andAlso("Assignment is to present local", () -> wizard.eq(name, to));
    andAlso("Local is not assigned in its later siblings", () -> !usedInLaterSiblings());
  }
  @Override public String description() {
    return "Consolidate declaration of " + name + " with its subsequent initialization";
  }
  @Override protected ASTRewrite go(final ASTRewrite $, final TextEditGroup g) {
    eliminateFragment($, g);
    $.replace(nextStatement, makeVariableDeclarationFragement(), g);
    return $;
  }
  @Override protected ASTNode[] span() {
    return new ASTNode[] { declaration, nextStatement };
  }
  @Override public Examples examples() {
    return convert("int a; a = b;")//
        .to("int a = b;")//
    ;
  }
}
