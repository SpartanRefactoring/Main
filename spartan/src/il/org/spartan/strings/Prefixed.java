// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.strings;

import org.jetbrains.annotations.*;

/** A <b>utility</b> class, providing services of determining whether a
 * {@link String} starts with a given {@link String} or one of a bunch of
 * {@link String}s. For example,
 *
 * <pre>
 * Prefixed.by(&quot;Hello&quot;, &quot;ll&quot;, &quot;He&quot;, &quot;low&quot;)
 * </pre>
 *
 * returns <code><b>true</b></code>, while both
 *
 * <pre>
 * Prefixed.by(&quot;Hello&quot;, &quot;hell&quot;);
 * </pre>
 *
 * and
 *
 * <pre>
 * Suffixed.by(&quot;Hello&quot;);
 * </pre>
 *
 * return <code><b>false</b></code>.
 * @author Yossi Gil, the Technion.
 * @since 24/08/2008 */
public enum Prefixed {
  ;
  /** Determine whether a string starts with any one of the supplied suffixes.
   * @param s a string to examine
   * @param prefixes a list of potential extensions
   * @return <code><b>true</b></code> <em>iff</em> <code>s</code> starts with
   *         any one of the supplied prefixes.. */
  public static boolean by(@NotNull final String s, @NotNull final String... prefixes) {
    for (@NotNull final String prefix : prefixes)
      if (s.startsWith(prefix))
        return true;
    return false;
  }
}
