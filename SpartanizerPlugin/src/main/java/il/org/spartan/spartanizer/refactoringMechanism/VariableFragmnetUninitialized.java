package il.org.spartan.spartanizer.refactoringMechanism;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.utils.*;

/** TODO dormaayn: document class
 * @author dormaayn <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-03-27 */
public abstract class VariableFragmnetUninitialized extends LocalVariable {
  private static final long serialVersionUID = 0x40D2B631F771C9F4L;

  public VariableFragmnetUninitialized() {
    andAlso(Proposition.of("Fragment must not be initialized", () -> initializer == null));
  }

  @Override public abstract String description(VariableDeclarationFragment f);
}