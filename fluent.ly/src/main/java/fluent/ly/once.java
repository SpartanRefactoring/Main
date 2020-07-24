// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package fluent.ly;

import il.org.spartan.Separator;

/**
 * A class to manage printing a {@link String} exactly once. In the first
 * invocation of {@link #toString()}, the initial value is returned. In all
 * subsequent invocations, the empty string is returned.
 *
 * @see Separator
 * @author Yossi Gil
 * @since 21/08/2007
 */
public class once {
  private String value;

  public once(final String value) {
    this.value = defaults.to(value, "");
  }

  @Override public String toString() {
    final String $ = value;
    value = null;
    return $;
  }
}
