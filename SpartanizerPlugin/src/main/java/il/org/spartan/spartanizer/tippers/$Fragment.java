package il.org.spartan.spartanizer.tippers;

import static java.util.stream.Collectors.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil <tt>yogi@cs.technion.ac.il</tt>
 * @since 2017-03-22 */
abstract class $Fragment extends CarefulTipper<VariableDeclarationFragment> {
  private static final long serialVersionUID = 1L;

  @Override public boolean prerequisite(@NotNull final VariableDeclarationFragment f) {
    if (haz.annotation(f))
      return false;
    if (f == null)
      return false;
    name = object().getName();
    parent = az.variableDeclarationStatement(f.getParent());
    nextStatement = extract.nextStatement(f);
    initializer = f.getInitializer();
    return true;
  }

  @Nullable @Override public Fragment tip(final VariableDeclarationFragment ¢) {
    assert ¢ == null;
    assert ¢ == object();
    return new Fragment(description(), object(), myClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        $Fragment.this.go(r, g);
      }
    };
  }

  protected boolean doesUseForbiddenSiblings(final ASTNode... ns) {
    return youngerSiblings().stream().anyMatch(λ -> collect.BOTH_SEMANTIC.of(λ).existIn(ns));
  }

  /** Eliminates a {@link VariableDeclarationFragment}, with any other fragment
   * fragments which are not live in the containing
   * {@link VariabelDeclarationStatement}. If no fragments are left, then this
   * containing node is eliminated as well.
   * @return */
  @NotNull protected final ASTRewrite eliminateFragment(@NotNull final ASTRewrite r, final TextEditGroup g) {
    final List<VariableDeclarationFragment> live = otherSiblings();
    if (live.isEmpty()) {
      r.remove(parent(), g);
      return r;
    }
    final VariableDeclarationStatement newParent = copy.of(parent());
    fragments(newParent).clear();
    fragments(newParent).addAll(live);
    r.replace(parent(), newParent, g);
    return r;
  }

  protected int eliminationSaving() {
    final List<VariableDeclarationFragment> live = otherSiblings();
    final int $ = metrics.size(parent());
    if (live.isEmpty())
      return $;
    final VariableDeclarationStatement newParent = copy.of(parent());
    fragments(newParent).clear();
    fragments(newParent).addAll(live);
    return $ - metrics.size(newParent);
  }

  @Nullable protected abstract ASTRewrite go(ASTRewrite r, TextEditGroup g);

  @Nullable protected final Expression initializer() {
    return initializer;
  }

  @NotNull protected final SimpleName name() {
    return name;
  }

  @Nullable protected final Statement nextStatement() {
    return nextStatement;
  }

  protected final List<VariableDeclarationFragment> otherSiblings() {
    return fragments(parent()).stream().filter(λ -> λ != object()).collect(toList());
  }

  @Nullable protected VariableDeclarationStatement parent() {
    return parent;
  }

  protected boolean usedInSubsequentInitializers() {
    for (@NotNull final VariableDeclarationFragment f : youngerSiblings())
      if (!collect.usesOf(name()).in(f.getInitializer()).isEmpty())
        return true;
    return false;
  }

  @NotNull protected final Collection<VariableDeclarationFragment> youngerSiblings() {
    @NotNull final Collection<VariableDeclarationFragment> $ = new ArrayList<>();
    boolean collecting = false;
    for (final VariableDeclarationFragment f : fragments(parent()))
      if (f == object())
        collecting = true;
      else if (collecting)
        $.add(f);
    return $;
  }

  /** Removes a {@link VariableDeclarationFragment}, leaving intact any other
   * fragment fragments in the containing {@link VariabelDeclarationStatement} .
   * Still, if the containing node is left empty, it is removed as well.
   * @param r
   * @param g */
  void remove(@NotNull final ASTRewrite r, final TextEditGroup g) {
    r.remove(parent().fragments().size() > 1 ? object() : parent(), g);
  }

  @Nullable private Expression initializer;
  @NotNull private SimpleName name;
  @Nullable private Statement nextStatement;
  @Nullable private VariableDeclarationStatement parent;
}