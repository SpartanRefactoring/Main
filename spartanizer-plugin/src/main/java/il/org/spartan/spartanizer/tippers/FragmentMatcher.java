package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.java.*;

/** TODO dormaayn: document class
 * @author dormaayn <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-03-27 */
public abstract class FragmentMatcher extends NodeMatcher<VariableDeclarationFragment> {
  private static final long serialVersionUID = -0x5D2F121B3027EFA6L;
  @Property protected Expression initializer;
  @Property protected SimpleName name;
  @Property protected String identifier;

  @Override protected ASTNode highlight() {
    return name;
  }
  protected FragmentMatcher() {
    property("Name", () -> name = current().getName());
    property("Identifier", () -> identifier = name.getIdentifier());
    property("Initializer", () -> initializer = current().getInitializer());
    andAlso("Inapplicable on annotated fragments", () -> !haz.annotation(current()));
  }
  protected Expression initializer() {
    return initializer;
  }
  final SimpleName name() {
    return name;
  }
}