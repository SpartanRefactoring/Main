package il.org.spartan.spartanizer.patterns;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.utils.*;

/** TODO dormaayn: document class
 * @author dormaayn <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-03-27 */
public abstract class LocalVariableInitialized extends LocalVariable {
  private static final long serialVersionUID = 0x40D2B631F771C9F4L;

  public LocalVariableInitialized() {
    andAlso(Proposition.of(//
        "Fragment must be initialized", //
        () -> initializer != null));
  }

  @Override public abstract String description(VariableDeclarationFragment f);
}
