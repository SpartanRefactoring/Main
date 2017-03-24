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
 * @author Yossi Gil {@code yogi@cs.technion.ac.il}
 * @since 2017-03-22 */
abstract class $Fragment extends CarefulTipper<VariableDeclarationFragment> {
  private static final long serialVersionUID = 0x54EEEFC48BF86611L;

  public $Fragment() {}

  @Override public boolean prerequisite(@NotNull final VariableDeclarationFragment ¢) {
    if (haz.annotation(¢) || ¢ == null)
      return false;
    name = object().getName();
    parent = az.variableDeclarationStatement(¢.getParent());
    nextStatement = extract.nextStatement(¢);
    initializer = ¢.getInitializer();
    return true;
  }

  @Override @Nullable public Tip tip(@Nullable final VariableDeclarationFragment ¢) {
    System.err.println("Tipping " + ¢ + ":" + myClass());
    assert ¢ != null;
    assert ¢ == object();
    return new Tip(description(), object(), myClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        $Fragment.this.go(r, g);
      }
    };
  }

  protected final boolean doesUseForbiddenSiblings(final ASTNode... ns) {
    return youngerSiblings().stream().anyMatch(λ -> collect.BOTH_SEMANTIC.of(λ).existIn(ns));
  }

  /** Eliminates a {@link VariableDeclarationFragment}, with any other fragment
   * fragments which are not live in the containing
   * {@link VariabelDeclarationStatement}. If no fragments are left, then this
   * containing node is eliminated as well.
   * @return */
  @NotNull protected final ASTRewrite eliminateFragment(@NotNull final ASTRewrite $, final TextEditGroup g) {
    final List<VariableDeclarationFragment> live = otherSiblings();
    if (live.isEmpty()) {
      $.remove(parent(), g);
      return $;
    }
    final VariableDeclarationStatement newParent = copy.of(parent());
    fragments(newParent).clear();
    fragments(newParent).addAll(live);
    $.replace(parent(), newParent, g);
    return $;
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
    for (@NotNull final VariableDeclarationFragment ¢ : youngerSiblings())
      if (!collect.usesOf(name()).in(¢.getInitializer()).isEmpty())
        return true;
    return false;
  }

  @NotNull protected final Collection<VariableDeclarationFragment> youngerSiblings() {
    @NotNull final Collection<VariableDeclarationFragment> $ = new ArrayList<>();
    boolean collecting = false;
    for (final VariableDeclarationFragment ¢ : fragments(parent()))
      if (¢ == object())
        collecting = true;
      else if (collecting)
        $.add(¢);
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