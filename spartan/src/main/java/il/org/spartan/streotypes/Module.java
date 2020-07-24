// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.streotypes;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** A <b>Designator</b> used for marking classes serving as modules. That is,
 * classes which allow structured access and manipulation of their encapsulated
 * <code>static</code> data.
 * @author Yossi Gil, the Technion.
 * @since 23/08/2008 */
@Documented //
@Retention(RetentionPolicy.SOURCE) //
@Target(ElementType.TYPE)
@Designator //
public @interface Module {
  // No members in a <b>Designator</b>.
}
