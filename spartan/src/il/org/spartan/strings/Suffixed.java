// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.strings;

import java.io.*;

import org.jetbrains.annotations.*;

import il.org.spartan.streotypes.*;

/** A <b>utility</b> class, providing services of determining whether a
 * {@link String} or a {@link File} is suffixed by a given {@link String}, or
 * one of a bunch of {@link String}s. For example,
 *
 * <pre>
 * Suffixed.by(&quot;Hello&quot;, &quot;wo&quot;, &quot;low&quot;, &quot;lo&quot;)
 * </pre>
 *
 * returns <code><b>true</b></code>, while both
 *
 * <pre>
 * Suffixed.by(&quot;Hello&quot;, &quot;He&quot;);
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
@Utility
public enum Suffixed {
  ;
  /** Determine whether a file name ends with any one of the supplied
   * extensions.
   * @param f a file to examine
   * @param suffixes a list of potential extensions.
   * @return <code><b>true</b></code> <em>iff</em>the file name ends with any
   *         one of the supplied extensions. */
  public static boolean by(@NotNull final File f, final String... suffixes) {
    return by(f.getName(), suffixes);
  }
  /** Determine whether a string ends with any one of the supplied suffixes.
   * @param s a string to examine
   * @param suffixes a list of potential suffixes
   * @return <code><b>true</b></code> <em>iff</em> <code>s</code> ends with any
   *         one of the supplied suffixes. */
  public static boolean by(@NotNull final String s, @NotNull final String... suffixes) {
    for (@NotNull final String end : suffixes)
      if (s.endsWith(end))
        return true;
    return false;
  }
}
