package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

/** TODO dormaayn: document class Note that Java currently does not allow
 * initializers to single varaible declarations
 * @author dormaayn <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-03-27 */
public abstract class ParameterCatchOrForInitialized extends NodePattern<SingleVariableDeclaration> {
  private static final long serialVersionUID = -0x5D2F121B3027EFA6L;
  protected Expression initializer;
  protected SimpleName name;
  protected Type type;
  @Property protected SingleVariableDeclaration currentDeclaration;

  protected ParameterCatchOrForInitialized() {
    andAlso("Illegal Definition", () -> {
      if (current().getInitializer() == null)
        return false;
      name = current().getName();
      type = current().getType();
      initializer = current().getInitializer();
      currentDeclaration = current();
      return true;
    });
  }
  protected Type type() {
    return type;
  }
  final SimpleName name() {
    return name;
  }
  protected Expression initializer() {
    return initializer;
  }
}