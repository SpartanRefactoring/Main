package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.Assignment.Operator.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.tipping.*;
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
    implements TipperCategory.Collapse {
  private static final long serialVersionUID = 0xCE4CF4E3910F992L;

  private VariableDeclarationFragment makeVariableDeclarationFragement() {
    final VariableDeclarationFragment $ = copy.of(current);
    $.setInitializer(copy.of(from));
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
    andAlso("New value does not use values of later siblings", () -> compute.usedNames(from).noneMatch(x -> x.equals(identifier)));
  }

  @Override public String description() {
    return "Consolidate declaration of " + name + " with its subsequent initialization";
  }

  @Override protected ASTRewrite go(final ASTRewrite $, final TextEditGroup g) {
    $.replace(current, makeVariableDeclarationFragement(), g);
    $.remove(nextStatement, g);
    return $;
  }

  @Override public Examples examples() {
    return convert("int a; a = b;")//
        .to("int a = b;")//
    ;
  }
}
