// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.streotypes;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** A <b>Designator</b> used for marking utility classes. That is, classes which
 * provide a bunch of <code><b>static</b></code> library functions, with no
 * state information.
 *
 * @author Yossi Gil, the Technion.
 * @since 23/08/2008 */
@Documented //
@Retention(RetentionPolicy.SOURCE) //
@Target(ElementType.TYPE) @Designator //
public @interface Utility {
  // No members in a <b>Designator</b>.
}
