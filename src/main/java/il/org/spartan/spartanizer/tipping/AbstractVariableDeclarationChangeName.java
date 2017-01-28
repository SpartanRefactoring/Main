package il.org.spartan.spartanizer.tipping;

import java.util.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;

/** A parent tipper for changing variables names
 * @author Ori Roth
 * @since 2016/05/08
 * @param <N> either SingleVariableDeclaration or VariableDeclarationFragment */
public abstract class AbstractVariableDeclarationChangeName<N extends VariableDeclaration> extends MultipleReplaceCurrentNode<N> {
  @Override public ASTRewrite go(final ASTRewrite r, final N n, @SuppressWarnings("unused") final TextEditGroup __, final List<ASTNode> uses,
      final List<ASTNode> replacement) {
    if (!change(n))
      return null;
    uses.addAll(collect.usesOf(n.getName()).in(containing.typeDeclaration(n)));
    replacement.add(replacement(n));
    return r;
  }

  protected abstract boolean change(N n);

  protected abstract SimpleName replacement(N n);
}
