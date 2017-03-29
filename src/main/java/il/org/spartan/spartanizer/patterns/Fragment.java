package il.org.spartan.spartanizer.patterns;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.java.*;
import il.org.spartan.utils.*;

/** TODO dormaayn: document class
 * @author dormaayn <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-03-27 */
public abstract class Fragment extends AbstractPattern<VariableDeclarationFragment> {
  private static final long serialVersionUID = -6714605477414039462L;
  protected Expression initializer;
  protected SimpleName name;
  protected VariableDeclarationFragment fragment;

  Fragment() {
    andAlso(Proposition.of("Inapplicable on annotated fragments", () -> {
      fragment = object();
      name = fragment.getName();
      initializer = fragment.getInitializer();
      if (haz.annotation(fragment))
        return false;
      return true;
    }));
  }
}