package il.org.spartan.spartanizer.tippers;

import static java.util.stream.Collectors.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** TODO Yossi Gil: document class
 * @author Yossi Gil <tt>yogi@cs.technion.ac.il</tt>
 * @since 2017-03-22 */
public abstract class $Fragment extends CarefulTipper<VariableDeclarationFragment> {
  protected static boolean forbidden(@NotNull final VariableDeclarationFragment f, @Nullable final Expression initializer) {
    return initializer == null || haz.annotation(f);
  }

  private static final long serialVersionUID = 1L;

  public int eliminationSaving() {
    final List<VariableDeclarationFragment> live = otherSiblings();
    final int $ = metrics.size(parent());
    if (live.isEmpty())
      return $;
    final VariableDeclarationStatement newParent = copy.of(parent());
    fragments(newParent).clear();
    fragments(newParent).addAll(live);
    return $ - metrics.size(newParent);
  }

  @Override public boolean prerequisite(@NotNull final VariableDeclarationFragment f) {
    if (haz.annotation(f))
      return false;
    if (f == null)
      return false;
    fragment = f;
    name = f.getName();
    parent = az.variableDeclarationStatement(f.getParent());
    nextStatement = extract.nextStatement(f);
    initializer = f.getInitializer();
    return true;
  }

  @Nullable
  @Override public Tip tip(final VariableDeclarationFragment ¢) {
    assert ¢ == object();
    return new Tip(description(), null, null) {
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
  @NotNull
  protected final ASTRewrite eliminateFragment(@NotNull final ASTRewrite r, final TextEditGroup g) {
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

  protected final VariableDeclarationFragment fragment() {
    return fragment;
  }

  @Nullable
  protected abstract ASTRewrite go(ASTRewrite r, TextEditGroup g);

  protected final Expression initializer() {
    return initializer;
  }

  protected final SimpleName name() {
    return name;
  }

  @Nullable
  protected final Statement nextStatement() {
    return nextStatement;
  }

  protected final List<VariableDeclarationFragment> otherSiblings() {
    return fragments(parent()).stream().filter(λ -> λ != fragment()).collect(toList());
  }

  @Nullable
  protected VariableDeclarationStatement parent() {
    return parent;
  }

  protected boolean usedInSubsequentInitializers() {
    boolean searching = true;
    for (@NotNull final VariableDeclarationFragment f : fragments(parent()))
      if (searching)
        searching = f != fragment();
      else if (!collect.usesOf(name()).in(f.getInitializer()).isEmpty())
        return true;
    return false;
  }

  @NotNull
  protected final Collection<VariableDeclarationFragment> youngerSiblings() {
    @NotNull final Collection<VariableDeclarationFragment> $ = new ArrayList<>();
    boolean collecting = false;
    for (final VariableDeclarationFragment f : fragments(parent()))
      if (f == fragment())
        collecting = true;
      else if (collecting)
        $.add(f);
    return $;
  }

  /** Removes a {@link VariableDeclarationFragment}, leaving intact any other
   * fragment fragments in the containing {@link VariabelDeclarationStatement} .
   * Still, if the containing node is left empty, it is removed as well.
   * @param f
   * @param r
   * @param g */
  void remove(@NotNull final ASTRewrite r, final TextEditGroup g) {
    r.remove(parent().fragments().size() > 1 ? fragment() : parent(), g);
  }

  private VariableDeclarationFragment fragment;
  private Expression initializer;
  private SimpleName name;
  @Nullable
  private Statement nextStatement;
  @Nullable
  private VariableDeclarationStatement parent;
}