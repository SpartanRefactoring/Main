package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil {@code yogi@cs.technion.ac.il}
 * @since 2017-03-22 */
public abstract class $FragmentInitializerNo extends $Fragment {
  private static final long serialVersionUID = 3636250496963900917L;

  @Override public boolean prerequisite(final VariableDeclarationFragment ¢) {
    return super.prerequisite(¢) && initializer() == null;
  }
}
