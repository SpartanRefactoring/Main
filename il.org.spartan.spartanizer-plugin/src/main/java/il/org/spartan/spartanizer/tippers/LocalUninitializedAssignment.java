package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.Assignment.Operator.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.tipping.categories.*;
import il.org.spartan.utils.*;

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
    final VariableDeclarationStatement ret = current.getAST().newVariableDeclarationStatement(f);
    ret.setType(copy.of(declaration.getType()));
    return ret;
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
