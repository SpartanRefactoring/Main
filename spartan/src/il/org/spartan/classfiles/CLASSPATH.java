package il.org.spartan.classfiles;

import java.io.*;
import java.net.*;

import org.eclipse.jdt.annotation.*;

import il.org.spartan.streotypes.*;
import il.org.spartan.utils.*;
import il.org.spatan.iteration.*;

/** A representation of the system global CLASSPATH.
 * @author Yossi Gil
 * @see JRE */
@Utility
public class CLASSPATH {
  /** A class loader, which represents a */
   private static ClassLoader classLoader;
  /** Which system property contains the class path? */
  private static final String JAVA_CLASS_PATH = "java.class.path";
  private static final String original = get();

  /** Append a location to the class path.
   * @param location a location to prepend to the class path. */
  public static void append(final String location) {
    set(get() + File.pathSeparator + location);
  }
  /** Retrieves the system's CLASSPATH in an array
   * @return the content of the CLASSPATH, broken into array entries */
  public static String[] asArray() {
    return get().split(File.pathSeparator);
  }
  /** Retrieves the system's CLASSPATH in an {@link Iterable}
   * @return the content of the CLASSPATH, broken into array entries */
   public static Iterable<String> asIterable() {
    return Iterables.make(asArray());
  }
   public static ClassLoader classLoader() {
    return classLoader != null ? classLoader : (classLoader = computeClassLoader());
  }
  /** Reset the class path and prepend multiple locations to the class path.
   * @param locations locations to prepend to the class path. */
  public static void fix( final String... locations) {
    reset();
    for (final String location : locations)
      prepend(location);
  }
  /** Retrieves the system's CLASSPATH as a string
   * @return the current system CLASSPATH */
  public static String get() {
    return System.getProperty(JAVA_CLASS_PATH);
  }
  /** Mocks the system's {@link Class#forName} function, but using a
   * {@link ClassLoader} that makes it possible to load classes according to the
   * <em>current</em> class path. (The default {@link ClassLoader} fails to
   * detect changes to the class made after system initialization.)
   * <p>
   * <b>Note:</b> Observe that in trying to load a class file, with or without
   * initialization, it could be the case that a certain <tt>.class</tt> class
   * file could be read and is legally structured, it could not be appropriately
   * loaded, since it refers to other classes which cannot be found. In this
   * case a {@link NoClassDefFoundError} exception is thrown.
   * @param className the name of the class to load.
   * @return the loaded {@link Class} object
   * @throws ClassNotFoundException in case the class could not be found.
   * @throws NoClassDefFoundError in case the class itself could be found, but
   *         some other class it refers to could not be found. */
  public static Class<?> initialize(final String className) throws ClassNotFoundException, NoClassDefFoundError {
    return Class.forName(className, true, classLoader());
  }
  /** Mocks {@link Class#forName(String, boolean, ClassLoader)}, where the
   * second parameter is <code><b>false</b></code>, and the third is our local
   * {@link ClassLoader} object. Thus, this function is similar to
   * {@link #initialize(String)}, except that the loaded class is not
   * initialized.
   * <p>
   * <b>Note:</b> Observe that in trying to load a class file, with or without
   * initialization, it could be the case that a certain <tt>.class</tt> class
   * file could be read and is legally structured, it could not be appropriately
   * loaded, since it refers to other classes which cannot be found. In this
   * case a {@link NoClassDefFoundError} exception is thrown.
   * @param className the name of the class to load.
   * @return the loaded {@link Class} object
   * @throws ClassNotFoundException in case the class could not be found.
   * @throws NoClassDefFoundError in case the class itself could be found, but
   *         some other class it refers to could not be found. */
  public static Class<?> load(final String className) throws ClassNotFoundException, NoClassDefFoundError {
    return Class.forName(className, false, classLoader());
  }
  /** Exercise this class by printing the result of its principal function.
   * @param __ unused */
  public static void main(final String[] __) {
    System.out.println(Separate.by(asArray(), "\n"));
  }
  /** Prepend a location to the class path.
   * @param location a location to prepend to the class path. */
  public static void prepend(final String location) {
    set(location + File.pathSeparator + get());
  }
  /** Sets the system's CLASSPATH to its original value */
  public static void reset() {
    set(original);
  }
  /** Sets the system's CLASSPATH
   * @param path the new value of the CLASSPATH */
  public static void set( final String path) {
    System.setProperty(JAVA_CLASS_PATH, path);
    classLoader = null;
  }
   private static ClassLoader computeClassLoader() {
     final String[] path = asArray();
     final URL[] $ = new URL[path.length];
    for (int i = 0; i < path.length; ++i)
      try {
        $[i] = new File(path[i]).toURI().toURL();
      } catch ( final MalformedURLException __) {
        // Ignore
      }
    return new URLClassLoader($);
  }
}
