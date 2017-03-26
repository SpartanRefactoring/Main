package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** TODO: Yossi Gil please add a description
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-07-29 */
public final class ModifierFinalAbstractMethodRedundant extends RemovingTipper<Modifier>//
    implements TipperCategory.SyntacticBaggage {
  private static final long serialVersionUID = 0x3057B6D28D0CEC1L;

  @Override public String description() {
    return "Remove redundant final modifier of paramaeter to abstract method";
  }

  @Override  public String description(final Modifier ¢) {
    return "Remove redundant final '" + az.singleVariableDeclaration(parent(¢)) + "' (parameter to abstract method)";
  }

  @Override public boolean prerequisite( final Modifier m) {
    if (!m.isFinal())
      return false;
    @Nullable final SingleVariableDeclaration v = az.singleVariableDeclaration(parent(m));
    if (v == null)
      return false;
    @Nullable final MethodDeclaration $ = az.methodDeclaration(parent(v));
    return $ != null && body($) == null;
  }
}
