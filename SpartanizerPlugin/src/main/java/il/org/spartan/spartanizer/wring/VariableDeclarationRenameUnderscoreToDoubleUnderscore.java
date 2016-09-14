package il.org.spartan.spartanizer.wring;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.wring.dispatch.*;
import il.org.spartan.spartanizer.wring.strategies.*;

/** Replaces name of variables named "_" into "__"
 * @author Ori Roth
 * @param <N> either SingleVariableDeclaration or VariableDeclarationFragment
 * @since 2016/05/08 */
public final class VariableDeclarationRenameUnderscoreToDoubleUnderscore<N extends VariableDeclaration> //
    extends AbstractVariableDeclarationChangeName<N> implements Kind.UnusedArguments {
  @Override protected boolean change(final N ¢) {
    return "_".equals(¢.getName() + "");
  }

  @Override public String description(@SuppressWarnings("unused") final N __) {
    return "Use double underscore instead a single underscore";
  }

  @Override protected SimpleName replacement(final N ¢) {
    return ¢.getAST().newSimpleName("__");
  }
}
