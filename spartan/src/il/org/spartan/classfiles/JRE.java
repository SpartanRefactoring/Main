// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.classfiles;

import java.io.*;
import java.net.*;
import java.util.*;

import org.jetbrains.annotations.*;

import il.org.spartan.streotypes.*;
import il.org.spartan.utils.*;

/** A class representing the location on the file system of the <em>Java Runtime
 * Environment</em> (JRE), that is the standard Java library.
 * <p>
 * Since Java does not yet have an API that provides this information the JRE
 * location is computed using two heuristics:
 * <ul>
 * <li>If the default class loader is an instance of {@link URLClassLoader},
 * then its {@link URLClassLoader#getURLs()} method is invoked to find the JRE.
 * <li>If the above fails, the system property "sun.boot.class.path" is fetched,
 * giving the JRE location in JVMs by Sun.
 * </ul>
 * @author Yossi Gil
 * @since 12/07/2007
 * @see CLASSPATH */
@Utility public enum JRE {
  ;
  /** retrieve the system's CLASSPATH
   * @return the content of the classpath, broken into array entries */
  @NotNull public static List<File> asList() {
    try {
      return fromClass(Object.class);
    } catch (@NotNull final Throwable __) {
      // Absorb this exception, let's try the other option...
      @NotNull final List<File> $ = new ArrayList<>();
      final String cp = System.getProperty("sun.boot.class.path");
      for (@NotNull final StringTokenizer ¢ = new StringTokenizer(cp, File.pathSeparator); ¢.hasMoreTokens();)
        $.add(new File(¢.nextToken()));
      return $;
    }
  }

  /** Obtain the CLASSPATH location used by the class loader of a given classes.
   * @param cs An array of classes
   * @return a list of files
   * @throws IllegalArgumentException If the class loader of <code>c</code> is
   *         not a URLClassLoader */
  @NotNull public static List<File> fromClass(@NotNull final Class<?>... cs) throws IllegalArgumentException {
    @NotNull final List<File> $ = new ArrayList<>();
    for (@NotNull final Class<?> c : cs) {
      final ClassLoader cl = c.getClassLoader();
      if (!(cl instanceof URLClassLoader))
        throw new IllegalArgumentException("Class loader is not a URLClassLoader. class=" + c.getName());
      for (@NotNull final URL url : ((URLClassLoader) cl).getURLs())
        try {
          $.add(new File(url.toURI()));
        } catch (@NotNull final URISyntaxException e) {
          throw new IllegalArgumentException("I cannot obtain a file from url " + url);
        }
    }
    return $;
  }

  /** Exercise this class, by printing the result of its principal function.
   * @param __ unused */
  public static void main(final String[] __) {
    System.out.println(Separate.by(asList(), "\n"));
  }
}
