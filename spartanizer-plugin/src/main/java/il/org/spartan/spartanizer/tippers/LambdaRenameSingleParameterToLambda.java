package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.parameters;

import org.eclipse.jdt.core.dom.LambdaExpression;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import fluent.ly.not;
import fluent.ly.the;
import il.org.spartan.spartanizer.ast.factory.misc;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.engine.nominal.JohnDoe;
import il.org.spartan.spartanizer.java.namespace.Environment;
import il.org.spartan.spartanizer.java.namespace.Namespace;
import il.org.spartan.spartanizer.research.analyses.notation;
import il.org.spartan.spartanizer.tipping.categories.Nominal;
import il.org.spartan.utils.Examples;

/** Tested by {@link Issue1115}
 * @author Yossi Gil
 * @since 2016-09 */
public final class LambdaRenameSingleParameterToLambda extends NodeMatcher<LambdaExpression>//
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
