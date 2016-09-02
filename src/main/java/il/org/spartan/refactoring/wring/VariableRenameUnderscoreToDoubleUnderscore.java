package il.org.spartan.refactoring.wring;

import org.eclipse.jdt.core.dom.*;

/** Replaces name of variables named "_" into "__"
 * @author Ori Roth
 * @param <N> either SingleVariableDeclaration or VariableDeclarationFragment
 * @since 2016/05/08 */
public final class VariableRenameUnderscoreToDoubleUnderscore<N extends VariableDeclaration> //
    extends VariableChangeName<N> implements Kind.UnusedArguments {
  @Override boolean change(final N n) {
    return "_".equals("" + n.getName());
  }

  @Override SimpleName replacement(final N n) {
    return n.getAST().newSimpleName("__");
  }

  @Override String description(@SuppressWarnings("unused") final N __) {
    return "Use double underscore instead a single underscore";
  }
}
