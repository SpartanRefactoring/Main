// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.streotypes;

import java.lang.annotation.*;

/** A <b>Designator</b> used for marking classes which serve as command line
 * applications.
 * @author Yossi Gil, the Technion.
 * @since 23/08/2008 */
@Documented //
@Retention(RetentionPolicy.SOURCE) //
@Target(ElementType.TYPE) @Designator //
public @interface Application {
  // No members in a <b>Designator</b>.
}
