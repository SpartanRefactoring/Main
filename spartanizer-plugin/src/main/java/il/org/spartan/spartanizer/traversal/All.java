package il.org.spartan.spartanizer.traversal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/** TODO Yossi Gil: document class 
 * 
 * @author Yossi Gil
 * @since 2017-07-17 */
@Target({ElementType.TYPE_PARAMETER, ElementType.TYPE_USE, ElementType.TYPE})
public @interface All {
}
