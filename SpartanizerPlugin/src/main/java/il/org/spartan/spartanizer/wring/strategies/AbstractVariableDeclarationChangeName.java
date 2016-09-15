package il.org.spartan.spartanizer.wring.strategies;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;

/** A parent wring for changing variables names TODO Ori: check safety of
 * Collect.usesOf(n.getName()).in(p)
 * @author Ori Roth
 * @since 2016/05/08
 * @param <N> either SingleVariableDeclaration or VariableDeclarationFragment */
public abstract class AbstractVariableDeclarationChangeName<N extends VariableDeclaration> extends MultipleReplaceCurrentNode<N> {
  protected abstract boolean change(N n);

  @Override public ASTRewrite go(final ASTRewrite r, final N n, @SuppressWarnings("unused") final TextEditGroup __, final List<ASTNode> uses,
      final List<ASTNode> replacement) {
    if (!change(n))
      return null;
    uses.addAll(Collect.usesOf(n.getName()).in(hop.containerType(n)));
    replacement.add(replacement(n));
    return r;
  }

  protected abstract SimpleName replacement(N n);
}
