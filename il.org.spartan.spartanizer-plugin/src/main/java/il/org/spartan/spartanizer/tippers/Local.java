package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** TODO dormaayn: document class
 * @author dormaayn <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-03-27 */
public abstract class Local extends FragmentAmongFragments {
  private static final long serialVersionUID = 0x54EEEFC48BF86611L;
  protected VariableDeclarationStatement declaration;

  public Local() {
    needs("Variable declation", //
        () -> declaration = az.variableDeclarationStatement(parent));
  }
  /** Eliminates a {@link VariableDeclarationFragment}, with any other fragment
   * fragments which are not live in the containing
   * {@link VariabelDeclarationStatement}. If no fragments are left, then this
   * containing node is eliminated as well.
   * @return */
  protected final ASTRewrite eliminateFragment(final ASTRewrite $, final TextEditGroup g) {
    final List<VariableDeclarationFragment> live = remainingSiblings();
    if (live.isEmpty())
      $.remove(declaration, g);
    else {
      final VariableDeclarationStatement newParent = copy.of(declaration);
      fragments(newParent).clear();
      fragments(newParent).addAll(live.stream().map(copy::of).collect(Collectors.toList()));
      $.replace(declaration, newParent, g);
    }
    return $;
  }
  protected int eliminationSaving() {
    final List<VariableDeclarationFragment> live = remainingSiblings();
    final int $ = metrics.size(declaration);
    if (live.isEmpty())
      return $;
    final VariableDeclarationStatement newParent = copy.of(declaration);
    fragments(newParent).clear();
    fragments(newParent).addAll(live);
    return $ - metrics.size(newParent);
  }
  /** Removes a {@link VariableDeclarationFragment}, leaving intact any other
   * fragment fragments in the containing {@link VariabelDeclarationStatement} .
   * Still, if the containing node is left empty, it is removed as well.
   * @param r
   * @param g */
  void remove(final ASTRewrite r, final TextEditGroup g) {
    r.remove(declaration.fragments().size() > 1 ? current() : declaration, g);
  }
  @Override protected final List<VariableDeclarationFragment> siblings() {
    return step.fragments(declaration);
  }
}