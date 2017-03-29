package il.org.spartan.spartanizer.patterns;

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

/** TODO dormaayn: document class
 * @author dormaayn <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-03-27 */
public abstract class LocalVariable extends Fragment {
  public static boolean doesUseForbiddenSiblings(final VariableDeclarationFragment f, final ASTNode... ns) {
    return wizard.forbiddenSiblings(f).stream().anyMatch(λ -> collect.BOTH_SEMANTIC.of(λ).existIn(ns));
  }
  private static final long serialVersionUID = 0x54EEEFC48BF86611L;

  public LocalVariable() {
    andAlso(Proposition.of("Must be local variable", () -> {
      containingVariableDeclarationStatement = az.variableDeclarationStatement(object().getParent());
      if (containingVariableDeclarationStatement == null)
        return false;
      return true;
    }));
  }

  protected  final boolean doesUseForbiddenSiblings(final ASTNode... ns) {
    return youngerSiblings().stream().anyMatch(λ -> collect.BOTH_SEMANTIC.of(λ).existIn(ns));
  }

  /** Eliminates a {@link VariableDeclarationFragment}, with any other fragment
   * fragments which are not live in the containing
   * {@link VariabelDeclarationStatement}. If no fragments are left, then this
   * containing node is eliminated as well.
   * @return */
  protected final ASTRewrite eliminateFragment(final ASTRewrite $, final TextEditGroup g) {
    final List<VariableDeclarationFragment> live = otherSiblings();
    if (live.isEmpty()) {
      $.remove(statement(), g);
      return $;
    }
    final VariableDeclarationStatement newParent = copy.of(statement());
    fragments(newParent).clear();
    fragments(newParent).addAll(live);
    $.replace(statement(), newParent, g);
    return $;
  }

  protected int eliminationSaving() {
    final List<VariableDeclarationFragment> live = otherSiblings();
    final int $ = metrics.size(statement());
    if (live.isEmpty())
      return $;
    final VariableDeclarationStatement newParent = copy.of(statement());
    fragments(newParent).clear();
    fragments(newParent).addAll(live);
    return $ - metrics.size(newParent);
  }

  protected final List<VariableDeclarationFragment> otherSiblings() {
    return fragments(statement()).stream().filter(λ -> λ != object()).collect(toList());
  }

  protected VariableDeclarationStatement statement() {
    return containingVariableDeclarationStatement;
  }

  protected boolean usedInSubsequentInitializers() {
    return !collect.usesOf(name).in(youngerSiblings()).isEmpty();
  }

  protected final Collection<VariableDeclarationFragment> youngerSiblings() {
    final Collection<VariableDeclarationFragment> $ = new ArrayList<>();
    boolean collecting = false;
    for (final VariableDeclarationFragment ¢ : fragments(statement()))
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
  void remove(final ASTRewrite r, final TextEditGroup g) {
    r.remove(statement().fragments().size() > 1 ? object() : statement(), g);
  }

  private VariableDeclarationStatement containingVariableDeclarationStatement;
}