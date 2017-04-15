package il.org.spartan.spartanizer.patterns;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tippers.*;

/** TODO dormaayn: document class
 * @author dormaayn <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-03-27 */
public abstract class FragmentPattern extends AbstractPattern<VariableDeclarationFragment> {
  private static final long serialVersionUID = -0x5D2F121B3027EFA6L;
  protected Expression initializer;
  protected SimpleName name;

  @Override protected ASTNode highlight() {
    return name;
  }

  protected FragmentPattern() {
    andAlso("Inapplicable on annotated fragments", () -> {
      if (haz.annotation(current()))
        return false;
      name = current().getName();
      initializer = current().getInitializer();
      return true;
    });
  }

  protected Expression initializer() {
    return initializer;
  }

  final SimpleName name() {
    return name;
  }

  public static boolean doesUseForbiddenSiblings(final VariableDeclarationFragment f, final ASTNode... ns) {
    return $FragmentAndStatement.forbiddenSiblings(f).stream().anyMatch(λ -> collect.BOTH_SEMANTIC.of(λ).existIn(ns));
  }
}