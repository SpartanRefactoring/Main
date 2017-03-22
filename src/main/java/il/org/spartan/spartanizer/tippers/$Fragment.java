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
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil <tt>yogi@cs.technion.ac.il</tt>
 * @since 2017-03-22 */
public abstract class $Fragment extends ReplaceToNextStatement<VariableDeclarationFragment> {
  private static final long serialVersionUID = 1L;

  protected final SimpleName name() {
    return name;
  }

  protected final Statement nextStatement() {
    return nextStatement;
  }

  protected final Expression initializer() {
    return initializer;
  }

  protected final VariableDeclarationFragment fragment() {
    return fragment;
  }

  /** Eliminates a {@link VariableDeclarationFragment}, with any other fragment
   * fragments which are not live in the containing
   * {@link VariabelDeclarationStatement}. If no fragments are left, then this
   * containing node is eliminated as well. */
  protected final void eliminateFragment(final ASTRewrite r, final TextEditGroup g) {
    final List<VariableDeclarationFragment> live = others();
    if (live.isEmpty()) {
      r.remove(parent(), g);
      return;
    }
    final VariableDeclarationStatement newParent = copy.of(parent());
    fragments(newParent).clear();
    fragments(newParent).addAll(live);
    r.replace(parent(), newParent, g);
  }

  protected final Collection<VariableDeclarationFragment> remainingSiblings() {
    final Collection<VariableDeclarationFragment> $ = new ArrayList<>();
    boolean collecting = false;
    for (final VariableDeclarationFragment f : fragments(parent()))
      if (f == fragment())
        collecting = true;
      else if (collecting)
        $.add(f);
    return $;
  }

  protected final List<VariableDeclarationFragment> others() {
    return fragments(parent()).stream().filter(λ -> λ != fragment()).collect(toList());
  }

  @Override public boolean prerequisite(final VariableDeclarationFragment f) {
    if (haz.annotation(f))
      return false;
    fragment = f;
    if (fragment == null)
    name = f.getName();
    parent = az.variableDeclarationStatement(f.getParent());
    nextStatement = extract.nextStatement(f);
    initializer = f.getInitializer();
    return go(ASTRewrite.create(f.getAST()), f, nextStatement, null) != null;
  }

  protected abstract ASTRewrite go(ASTRewrite r, TextEditGroup g);

  @Override protected final ASTRewrite go(final ASTRewrite r, final VariableDeclarationFragment f, final Statement nextStatement,
      final TextEditGroup g) {
    return go(r, g);
  }

  protected VariableDeclarationStatement parent() {
    return parent;
  }

  private VariableDeclarationFragment fragment;
  private SimpleName name;
  private VariableDeclarationStatement parent;
  private Expression initializer;
  private Statement nextStatement;
}