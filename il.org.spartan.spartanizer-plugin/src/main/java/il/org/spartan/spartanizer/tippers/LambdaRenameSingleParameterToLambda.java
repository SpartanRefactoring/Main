package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.java.namespace.*;
import il.org.spartan.spartanizer.research.analyses.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** Tested by {@link Issue1115}
 * @author Yossi Gil
 * @since 2016-09 */
public final class LambdaRenameSingleParameterToLambda extends NodePattern<LambdaExpression>//
    implements Nominal.Trivialization {
  private static final long serialVersionUID = -0x2CF705A7699A0E07L;
  private VariableDeclarationFragment fragment;
  private SimpleName name;
  private String identifier;
  private Namespace namespace;

  public LambdaRenameSingleParameterToLambda() {
    needs("Fragment", () -> fragment = az.variableDeclrationFragment(the.onlyOneOf(parameters(current))));
    property("Name", () -> name = fragment.getName());
    property("Identifier", () -> identifier = name + "");
    andAlso("Identifier must not be special", //
        () -> not.in(identifier, notation.lambda, notation.anonymous, notation.forbidden));
    andAlso("Identifier must be John Doe", //
        () -> JohnDoe.property(identifier));
    needs("Namespace", () -> namespace = Environment.of(current));
    andAlso("New name is free", () -> !namespace.has(notation.lambda));
    andAlso("No nested names", () -> !namespace.hasChildren());
  }
  @Override public String description() {
    return "Rename lambda parameter " + name + " to " + notation.lambda;
  }
  @Override protected ASTRewrite go(final ASTRewrite r, final TextEditGroup g) {
    misc.rename(name, current.getAST().newSimpleName(notation.lambda), current, r, g);
    return r;
  }
  @Override public Examples examples() {
    return null;
  }
}
