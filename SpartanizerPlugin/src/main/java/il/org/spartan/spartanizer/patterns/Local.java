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

/** TODO dormaayn: document class
 * @author dormaayn <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-03-27 */
public abstract class Local extends Fragment {
  private static final long serialVersionUID = 0x54EEEFC48BF86611L;
  protected VariableDeclarationStatement declaration;

  public Local() {
    andAlso("Must be local variable", () -> (declaration = az.variableDeclarationStatement(parent)) != null);
  }

  /** Eliminates a {@link VariableDeclarationFragment}, with any other fragment
   * fragments which are not live in the containing
   * {@link VariabelDeclarationStatement}. If no fragments are left, then this
   * containing node is eliminated as well.
   * @return */
  protected final ASTRewrite eliminateFragment(final ASTRewrite $, final TextEditGroup g) {
    final List<VariableDeclarationFragment> live = otherSiblings();
    if (live.isEmpty()) {
      $.remove(declaration, g);
      return $;
    }
    final VariableDeclarationStatement newParent = copy.of(declaration);
    fragments(newParent).clear();
    fragments(newParent).addAll(live);
    $.replace(declaration, newParent, g);
    return $;
  }

  protected int eliminationSaving() {
    final List<VariableDeclarationFragment> live = otherSiblings();
    final int $ = metrics.size(declaration);
    if (live.isEmpty())
      return $;
    final VariableDeclarationStatement newParent = copy.of(declaration);
    fragments(newParent).clear();
    fragments(newParent).addAll(live);
    return $ - metrics.size(newParent);
  }

  protected final List<VariableDeclarationFragment> otherSiblings() {
    return fragments(declaration).stream().filter(λ -> λ != current()).collect(toList());
  }

  protected boolean usedInSubsequentInitializers() {
    return youngerSiblings().stream().anyMatch(λ -> !collect.usesOf(name()).in(λ.getInitializer()).isEmpty());
  }

  protected final Collection<VariableDeclarationFragment> youngerSiblings() {
    final Collection<VariableDeclarationFragment> $ = new ArrayList<>();
    boolean collecting = false;
    for (final VariableDeclarationFragment ¢ : fragments(declaration))
      if (¢ == current())
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
    r.remove(declaration.fragments().size() > 1 ? current() : declaration, g);
  }
}