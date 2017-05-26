package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

/** TODO dormaayn: document class Note that Java currently does not allow
 * initializers to single varaible declarations
 * @author dormaayn <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-03-27 */
public abstract class ParameterCatchOrFor extends NodePattern<SingleVariableDeclaration> {
  private static final long serialVersionUID = -0x5D2F121B3027EFA6L;
  protected Expression initializer;
  protected SimpleName name;
  protected Type type;
  protected String identifier;

  ParameterCatchOrFor() {
    property("Name", () -> name = current.getName());
    property("Identifier", () -> identifier = name + "");
    property("Type", () -> type = current.getType());
    property("Initializer", () -> initializer = current.getInitializer());
  }
  protected Type type() {
    return type;
  }
  final SimpleName name() {
    return name;
  }
}