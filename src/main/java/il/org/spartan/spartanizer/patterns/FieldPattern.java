package il.org.spartan.spartanizer.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-04-15 */
public abstract class FieldPattern extends FragmentAmongFragmentsPattern {
  private static final long serialVersionUID = 0x54EEEFC48BF86611L;
  protected FieldDeclaration declaration;

  public FieldPattern() {
    andAlso("Must be field variable", () -> (declaration = az.fieldDeclaration(parent)) != null);
  }

  @Override protected final List<VariableDeclarationFragment> siblings() {
    return step.fragments(declaration);
  }

  protected boolean usedInSubsequentInitializers() {
    return youngerSiblings().stream().anyMatch(λ -> !collect.usesOf(name()).in(λ.getInitializer()).isEmpty());
  }

  final boolean doesUseForbiddenSiblings(final ASTNode... ns) {
    return youngerSiblings().stream().anyMatch(λ -> collect.BOTH_SEMANTIC.of(λ).existIn(ns));
  }

  /** Removes a {@link VariableDeclarationFragment}, leaving intact any other
   * fragment fragments in the containing {@link VariabelDeclarationStatement} .
   * Still, if the containing node is left empty, it is removed as well.
   * @param r
   * @param g */
  void remove(final ASTRewrite r, final TextEditGroup g) {
    r.remove(declaration.fragments().size() > 1 ? current() : declaration, g);
  }
}