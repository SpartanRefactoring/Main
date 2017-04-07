package il.org.spartan.spartanizer.tippers;

import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

/** TODO Yuval Simon: document class
 * @author Yuval Simon <tt>siyuval@campus.technion.ac.il</tt>
 * @since 2017-04-07 */
public final class Names {
  public static BiFunction<Type, MethodDeclaration, String> methodReturnName = (x, y) -> "$";
}
