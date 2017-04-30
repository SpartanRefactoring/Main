package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.Assignment.Operator.*;

import static java.util.stream.Collectors.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** See {@link #examples()}
 * @author Yossi Gil
 * @since 2015-08-07 */
public final class LocaUninitializedAssignment extends LocalUninitialized implements TipperCategory.Collapse {
  private static final long serialVersionUID = 0x14812B0904DFB002L;
  private Assignment assignment;
  private Expression from;
  private Expression to;

  public LocaUninitializedAssignment() {
    notNil("Next statement is an assignment", () -> assignment = extract.assignment(nextStatement));
    andAlso("It is a non-update assignment ", () -> assignment.getOperator() == ASSIGN);
    property("To", () -> to = to(assignment));
    property("From", () -> from = from(assignment));
    andAlso("Assignment is to present local", () -> wizard.eq(name, to));
    andAlso("Local is not assigned in its later siblings", () -> !usedInLaterSiblings());
    andAlso("New value does not use values of later siblings", () -> {
      final List<String> usedNames = compute.usedIdentifiers(from).collect(toList());
      return laterSiblings().noneMatch(λ -> usedNames.contains(λ.getName()));
    });
  }

  @Override public String description() {
    return "Consolidate declaration of " + name + " with its subsequent initialization";
  }

  @Override public Examples examples() {
    return convert("class A{{int a;a=b;}}")//
        .to("class A{{int a = b;}}")//
    ;
  }

  private VariableDeclarationFragment replacement() {
    final VariableDeclarationFragment $ = copy.of(current);
    $.setInitializer(copy.of(from));
    return $;
  }

  @Override protected ASTRewrite go(final ASTRewrite $, final TextEditGroup g) {
    $.replace(initializer, replacement(), g);
    remove.statement(nextStatement, $, g);
    return $;
  }
}
