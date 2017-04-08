package il.org.spartan.spartanizer.tippers;

import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

/** Contains methods for renaming return variables, parameters, etc.
 * @author Yuval Simon <tt>siyuval@campus.technion.ac.il</tt>
 * @author Dor Ma'ayan
 * @since 2017-04-07 */
public final class Names {
  public static BiFunction<Type, MethodDeclaration, String> methodReturnName = (x, y) -> "$";
  public static BiFunction<Type, MethodDeclaration, String> methodSingleParameterName = (x, y) -> "¢";
}
