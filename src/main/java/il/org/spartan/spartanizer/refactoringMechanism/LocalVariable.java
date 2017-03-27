package il.org.spartan.spartanizer.refactoringMechanism;

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
import il.org.spartan.utils.*;

/**
 * TODO dormaayn: document class 
 * @author dormaayn <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-03-27
 */
public abstract class LocalVariable extends Fragment {
  private static final long serialVersionUID = 0x54EEEFC48BF86611L;
  private Statement nextStatement;
  private VariableDeclarationStatement parent;

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
  protected final ASTRewrite eliminateFragment(final ASTRewrite $, final TextEditGroup g) {
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

  protected final Statement nextStatement() {
    return nextStatement;
  }

  protected final List<VariableDeclarationFragment> otherSiblings() {
    return fragments(parent()).stream().filter(λ -> λ != object()).collect(toList());
  }

  protected VariableDeclarationStatement parent() {
    return parent;
  }

  protected boolean usedInSubsequentInitializers() {
    return youngerSiblings().stream().anyMatch(λ -> !collect.usesOf(name()).in(λ.getInitializer()).isEmpty());
  }

  protected final Collection<VariableDeclarationFragment> youngerSiblings() {
    final Collection<VariableDeclarationFragment> $ = new ArrayList<>();
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
  void remove(final ASTRewrite r, final TextEditGroup g) {
    r.remove(parent().fragments().size() > 1 ? object() : parent(), g);
  }
}