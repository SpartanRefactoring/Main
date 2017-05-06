// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.streotypes;

import java.lang.annotation.*;

/** A <b>Designator</b> used for marking classical classes. That is, classes
 * which encapsulate data providing structured means for accessing and
 * manipulating this data.
 * @author Yossi Gil, the Technion.
 * @since 23/08/2008 */
@Documented //
@Retention(RetentionPolicy.SOURCE) //
@Target(ElementType.TYPE)
@Designator //
public @interface Classical {
  // No members in a <b>Designator</b>.
}
