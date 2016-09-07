// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.spartanizer.utils;

/** A class to manage printing a {@link String} exactly once. In the first
 * invocation of {@link #toString()}, the initial value is returned. In all
 * subsequent invocations, the empty string is returned.
 * @see Separator
 * @author Yossi Gil
 * @since 21/08/2007 */
public class Once {
  private String value;

  public Once(final String value) {
    this.value = Defaults.to(value, "");
  }

  @Override public String toString() {
    final String $ = value;
    value = null;
    return $;
  }
}
