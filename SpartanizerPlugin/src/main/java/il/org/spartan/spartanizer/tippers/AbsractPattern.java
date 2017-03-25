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
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-03-25 */
public abstract class AbsractPattern<N extends ASTNode> extends CarefulTipper<N> {
  private static final long serialVersionUID = 8732825589260890812L;
  private @NotNull Proposition prerequisite;

  protected AbsractPattern() {
    this.prerequisite = Proposition.T;
  }

  @Override public final boolean prerequisite(@NotNull final N ¢) {
    assert object() == ¢;
    return prerequisite.eval();
  }

  @Override @Nullable public final Tip tip(@Nullable final N n) {
    assert n != null;
    assert n == object();
    return new Tip(description(), object(), myClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        go(r, g);
      }
    };
  }

  protected AbsractPattern<N> andAlso(final Proposition ¢) {
    this.prerequisite = prerequisite.and(¢);
    return this;
  }

  @Nullable protected abstract ASTRewrite go(ASTRewrite r, TextEditGroup g);

  protected AbsractPattern<N> orElse(final Proposition ¢) {
    this.prerequisite = prerequisite.or(¢);
    return this;
  }
}

abstract class Fragment extends AbsractPattern<VariableDeclarationFragment> {
  private static final long serialVersionUID = -6714605477414039462L;
  @Nullable Expression initializer;
  @NotNull SimpleName name;

  Fragment() {
    andAlso(new Proposition.P("Inapplicable on annotated fragments", () -> {
      if (haz.annotation(object()))
        return false;
      name = object().getName();
      initializer = object().getInitializer();
      return true;
    }));
  }

  @Nullable Expression initializer() {
    return initializer;
  }

  @NotNull final SimpleName name() {
    return name;
  }
}

abstract class LocalVariable extends Fragment {
  private static final long serialVersionUID = 0x54EEEFC48BF86611L;
  @Nullable private Statement nextStatement;
  @Nullable private VariableDeclarationStatement parent;

  public LocalVariable() {
    andAlso(Proposition.of("Must be local variable", () -> {
      parent = az.variableDeclarationStatement(object().getParent());
      if (parent == null)
        return false;
      nextStatement = extract.nextStatement(object());
      return true;
    }));
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
    return youngerSiblings().stream().anyMatch(λ -> !collect.usesOf(name()).in(λ.getInitializer()).isEmpty());
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

  final boolean doesUseForbiddenSiblings(final ASTNode... ns) {
    return youngerSiblings().stream().anyMatch(λ -> collect.BOTH_SEMANTIC.of(λ).existIn(ns));
  }

  /** Removes a {@link VariableDeclarationFragment}, leaving intact any other
   * fragment fragments in the containing {@link VariabelDeclarationStatement} .
   * Still, if the containing node is left empty, it is removed as well.
   * @param r
   * @param g */
  void remove(@NotNull final ASTRewrite r, final TextEditGroup g) {
    r.remove(parent().fragments().size() > 1 ? object() : parent(), g);
  }
}

abstract class LocalVariableInitialized extends LocalVariable {
  private static final long serialVersionUID = 0x40D2B631F771C9F4L;

  public LocalVariableInitialized() {
    andAlso(Proposition.of("Frgament must be initialized", () -> initializer != null));
  }

  @Override @NotNull public abstract String description(VariableDeclarationFragment f);
}
abstract class LocalVariableUninitialized extends LocalVariable {
  private static final long serialVersionUID = 0x40D2B631F771C9F4L;

  public LocalVariableUninitialized() {
    andAlso(Proposition.of("Frgament must not be initialized", () -> initializer == null));
  }

  @Override @NotNull public abstract String description(VariableDeclarationFragment f);
}
abstract class LocalVariableInitializedStatement extends LocalVariableInitialized {
  private static final long serialVersionUID = 0x1B70489B1D1340L;

  @Override public final Tip tip(@NotNull final VariableDeclarationFragment f, @Nullable final ExclusionManager exclude) {
    @Nullable final Tip $ = super.tip(f, exclude);
    if ($ != null && exclude != null)
      exclude.exclude(f.getParent());
    return $;
  }
}
