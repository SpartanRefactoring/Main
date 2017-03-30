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

  Fragment() {
    andAlso(new Proposition.Singleton("Inapplicable on annotated fragments", () -> {
      if (haz.annotation(current()))
        return false;
      name = current().getName();
      initializer = current().getInitializer();
      return true;
    }));
  }

  protected Expression initializer() {
    return initializer;
  }

  final SimpleName name() {
    return name;
  }
}