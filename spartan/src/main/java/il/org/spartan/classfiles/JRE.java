// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.classfiles;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import fluent.ly.forget;
import il.org.spartan.streotypes.Utility;
import il.org.spartan.utils.Separate;

/**
 * A class representing the location on the file system of the <em>Java Runtime
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
 *
 * @author Yossi Gil
 * @since 12/07/2007
 * @see CLASSPATH
 */
@Utility
public enum JRE {
	;
	/**
	 * retrieve the system's CLASSPATH
	 *
	 * @return the content of the classpath, broken into array entries
	 */
	public static List<File> asList() {
		try {
			return fromClass(Object.class);
		} catch (final Throwable x) {
			forget.it(x);
			// Absorb this exception, let's try the other option...
			final List<File> $ = new ArrayList<>();
			final String cp = System.getProperty("sun.boot.class.path");
			for (final StringTokenizer ¢ = new StringTokenizer(cp, File.pathSeparator); ¢.hasMoreTokens();)
				$.add(new File(¢.nextToken()));
			return $;
		}
	}

	/**
	 * Obtain the CLASSPATH location used by the class loader of a given classes.
	 *
	 * @param cs An array of classes
	 * @return a list of files
	 * @throws IllegalArgumentException If the class loader of <code>c</code> is not
	 *                                  a URLClassLoader
	 */
	public static List<File> fromClass(final Class<?>... cs) throws IllegalArgumentException {
		final List<File> $ = new ArrayList<>();
		for (final Class<?> c : cs) {
			final ClassLoader cl = c.getClassLoader();
			if (!(cl instanceof URLClassLoader))
				throw new IllegalArgumentException("Class loader is not a URLClassLoader. class=" + c.getName());
			for (final URL url : ((URLClassLoader) cl).getURLs())
				try {
					$.add(new File(url.toURI()));
				} catch (final URISyntaxException e) {
					throw new IllegalArgumentException(e + ": " + url);
				}
		}
		return $;
	}

	/**
	 * Exercise this class, by printing the result of its principal function.
	 *
	 * @param __ unused
	 */
	public static void main(final String[] __) {
		System.out.println(Separate.by(asList(), "\n"));
	}
}
