package il.org.spartan.spartanizer.tipping;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** A parent tipper for changing variables names
 * @author Ori Roth
 * @since 2016/05/08
 * @param <N> either SingleVariableDeclaration or VariableDeclarationFragment */
public abstract class AbstractVariableDeclarationChangeName<N extends VariableDeclaration> extends MultipleReplaceCurrentNode<N> {
  @Override @Nullable public ASTRewrite go(final ASTRewrite r, @NotNull final N n, @SuppressWarnings("unused") final TextEditGroup __,
      @NotNull final List<ASTNode> uses, @NotNull final List<ASTNode> replacement) {
    if (!change(n))
      return null;
    uses.addAll(collect.usesOf(n.getName()).in(containing.typeDeclaration(n)));
    replacement.add(replacement(n));
    return r;
  }

  protected abstract boolean change(N n);

  @NotNull protected abstract SimpleName replacement(N n);
}
