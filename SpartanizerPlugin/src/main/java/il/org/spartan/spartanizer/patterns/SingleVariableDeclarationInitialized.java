package il.org.spartan.spartanizer.patterns;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.utils.*;

/** TODO dormaayn: document class Note that Java currently does not allow
 * initializers to single varaible declarations
 * @author dormaayn <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-03-27 */
public abstract class SingleVariableDeclarationInitialized extends AbstractPattern<SingleVariableDeclaration> {
  private static final long serialVersionUID = -6714605477414039462L;
  protected Expression initializer;
  protected SimpleName name;
  protected Type type;
  @Property
  protected SingleVariableDeclaration currentDeclaration;


  protected SingleVariableDeclarationInitialized() {
    andAlso(new Proposition.Singleton("Illegal Definition", () -> {
      if (current().getInitializer() == null)
        return false;
      name = current().getName();
      type = current().getType();
      initializer = current().getInitializer();
      currentDeclaration = current();
      return true;
    }));
  }

  protected Type type() {
    return type;
  }

  final SimpleName name() {
    return name;
  }
  
  protected Expression initializer(){
    return initializer;
  }
}