package il.org.spartan.classfiles;

import static fluent.ly.azzert.fail;
import static fluent.ly.azzert.is;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.junit.Test;

import fluent.ly.Iterables;
import fluent.ly.___;
import fluent.ly.azzert;
import il.org.spartan.utils.Separate;

/** A data structure representing a Java-like CLASSPATH, i.e., a collection of
 * names of folders and archive files. Tacit is the convention that Java class
 * files are found in these, by replacing each package name by a folder name.
 * <p>
 * This class offers enumeration service over all classes found in the
 * repository. The enumerated classes are represented as a collection of
 * {@link String}, where each string is the fully-qualified Java name of a
 * class, in the same format as required for {@link Class#forName(String)}.
 *
 * @author Itay Maman July 6, 2006 */
public class ClassRepository implements Iterable<String> {
  private static final String DOT_CLASS = ".class";
  private static final String DOT_JAR = ".jar";
  private static final String DOT_ZIP = ".zip";

  /** Obtain the CLASSPATH (as a list of files) from the class loaders of the
   * given classes.
   *
   * @param cs an array of classes to search for
   * @return a list of files
   * @throws IllegalArgumentException If the class loader of the arguments is not
   *                                  a URLClassLoader */
  public static List<File> fromClass(final Class<?>... cs) throws IllegalArgumentException {
    final List<File> $ = new ArrayList<>();
    for (final Class<?> c : cs) {
      final ClassLoader cl = c.getClassLoader();
      if (!(cl instanceof URLClassLoader))
        throw new IllegalArgumentException("Class loader is not a URLClassLoader. class=" + c.getName());
      ___.sure(((URLClassLoader) cl).getURLs().length > 0);
      for (final URL url : ((URLClassLoader) cl).getURLs())
        try {
          $.add(new File(url.toURI()));
        } catch (final URISyntaxException e) {
          $.add(new File(url.getFile().substring(1)));
          // continue;
        }
    }
    ___.sure(!$.isEmpty());
    return $;
  }

  /** Obtain a {@link ClassRepository} object that is initialized with the
   * location of the JRE on the local machine. Note that Java does not have an API
   * that provides this information so we have to make an intelligent guess as to
   * its whereabouts, follows:
   * <ul>
   * <li>If the default class loader is an instance of {@link URLClassLoader},
   * then use its getURLs() method.
   * <li>Use the system property "sun.boot.class.path" which points to the JRE
   * location, but only in JVMs by Sun.
   * </ul>
   *
   * @return A new ClassPathInfo object */
  public static List<File> fromJre() {
    try {
      return fromClass(Object.class);
    } catch (@SuppressWarnings("unused") final Throwable t) {
      // Abosrb, let's try the other option...
    }
    final List<File> $ = new ArrayList<>();
    final String cp = System.getProperty("sun.boot.class.path");
    for (final StringTokenizer ¢ = new StringTokenizer(cp, File.pathSeparator); ¢.hasMoreTokens();)
      $.add(new File(¢.nextToken()));
    return filter($);
  }

  public static void main(final String[] args) {
    final ClassRepository r = new ClassRepository.DEFAULT();
    System.out.println("Size is " + r.getClasses().size());
    final List<String> list = r.getClasses();
    for (int ¢ = 0; ¢ < list.size(); ++¢)
      System.out.println(¢ + " " + list.get(¢));
  }

  /** Adding all classes residing in archive to cache
   *
   * @param jarFile fill path to a jar file
   * @param result  List where scanned files are stored */
  private static void addFromArchive(final String jarFile, final ArrayList<String> result) {
    if (!new File(jarFile).exists())
      return;
    try (ZipFile zipFile = new ZipFile(jarFile)) {
      for (final Enumeration<? extends ZipEntry> entries = zipFile.entries(); entries.hasMoreElements();) {
        final ZipEntry ze = entries.nextElement();
        final NameDotSuffix nds = new NameDotSuffix(ze);
        if (nds.suffixIs(DOT_CLASS))
          result.add(nds.name);
      }
    } catch (final IOException ¢) {
      throw new RuntimeException("Damaged zip file: " + jarFile, ¢);
    }
  }

  private static String concat(final String path, final String name) {
    return (path == null || path.length() == 0 ? "" : path + ".") + name;
  }

  private static List<File> filter(final Iterable<File> fs, final File... ignore) {
    final List<File> $ = new ArrayList<>();
    for (File ¢ : fs) {
      ¢ = ¢.getAbsoluteFile();
      if (¢.exists() && !onDirs(¢, ignore))
        $.add(¢);
    }
    return $;
  }

  private static boolean onDir(final File f, final File d) {
    for (File parent = f; parent != null; parent = parent.getParentFile())
      if (parent.equals(d))
        return true;
    return false;
  }

  private static boolean onDirs(final File f, final File... dirs) {
    for (final File d : dirs)
      if (onDir(f, d.getAbsoluteFile()))
        return true;
    return false;
  }

  private static File[] toFile(final String[] paths) {
    final File[] $ = new File[paths.length];
    int i = 0;
    for (final String path : paths)
      $[i++] = new File(path).getAbsoluteFile();
    return $;
  }

  /** This is where the collection of elements of the class path are stored. */
  private final File[] files;

  /** Initialize a new empty instance */
  public ClassRepository() {
    files = new File[0];
  }

  public ClassRepository(final Class<?> me) {
    this(fromClass(me));
    ___.sure(files.length > 0);
    ___.sure(size() > 0);
  }

  /** Initialize a new instance
   *
   * @param fs Array of Files */
  public ClassRepository(final File... fs) {
    files = new File[fs.length];
    int i = 0;
    for (final File ¢ : fs)
      files[i++] = ¢.getAbsoluteFile();
  }

  /** Initialize a new instance from an iterable collection.
   *
   * @param fs an iterable collection of files. */
  public ClassRepository(final Iterable<File> fs) {
    this(Iterables.toArray(fs, File.class));
  }

  /** Initialize a new instance
   *
   * @param paths Array of strings. Each element is a path to a single file system
   *              location */
  public ClassRepository(final String... paths) {
    this(toFile(paths));
  }

  public ClassRepository(final URI uri) {
    this(new File(uri));
  }

  /** Find all classes on the CLASSPATH represented by the receiver
   *
   * @return List of fully qualified names of all such classes */
  public ArrayList<String> getClasses() {
    final ArrayList<String> $ = new ArrayList<>();
    for (final File ¢ : files)
      addFromDirectory(0, ¢, ¢.getAbsolutePath(), $, "");
    return $;
  }

  /** Obtain all starting point of the underlying class path
   *
   * @return Array of files */
  public File[] getRoots() {
    return Arrays.copyOf(files, files.length);
  }

  /** Obtain an iterator over all class names found in the class path
   *
   * @return a new iterator object */
  @Override public Iterator<String> iterator() {
    try {
      return getClasses().iterator();
    } catch (final Exception ¢) {
      throw new RuntimeException(¢);
    }
  }

  public final int size() {
    return getClasses().size();
  }

  @Override public String toString() {
    return Separate.by(files, File.pathSeparator);
  }

  /** Recursively adding all classes residing in specified directory into cache.
   *
   * @param depth     0-based depth inside the directory tree
   * @param dirOrFile file or directory
   * @param root      the root directory
   * @param result    List where results are stored
   * @param path      Relative path (dot-separated) from the starting point */
  private void addFromDirectory(final int depth, final File dirOrFile, final String root,
      final ArrayList<String> result, final String path) {
    if (dirOrFile.isDirectory()) {
      final String[] children = dirOrFile.list();
      for (final String ¢ : children)
        addFromDirectory(depth + 1, new File(dirOrFile, ¢), root, result,
            depth == 0 ? "" : concat(path, dirOrFile.getName()));
      return;
    }
    final NameDotSuffix nds = new NameDotSuffix(dirOrFile);
    if (nds.suffixIs(DOT_JAR) || nds.suffixIs(DOT_ZIP))
      addFromArchive(dirOrFile.getPath(), result);
    else if (nds.suffixIs(DOT_CLASS))
      result.add(concat(path, nds.name));
  }

  /** A specialized version of {@link ClassRepository} which contains the entire
   * class path.
   *
   * @author Yossi Gil
   * @since 13/09/2007 */
  public static class DEFAULT extends ClassRepository {
    public DEFAULT() {
      super(CLASSPATH.asArray());
    }
  }

  /** A specialized version of {@link ClassRepository} which contains the JRE
   *
   * @author Yossi Gil
   * @since 13/09/2007 */
  public static class JRE extends ClassRepository {
    public JRE() {
      super(fromJre());
    }
  }

  @SuppressWarnings("static-method") public static class TEST {
    @Test public void empty() {
      azzert.that(new ClassRepository().getClasses().size(), is(0));
    }

    // TODO Yossi: using non existing classes
    @Test public void ensureDotSeparatedNames() {
      fail("See TODO at ClassRepository");
      // try {
      // final List<File> fs = il.org.spartan.classfiles.JRE.asList();
      // fs.addAll(ClassRepository.fromClass(ClassRepositoryTest.class));
      // final ClassRepository cpi = new ClassRepository(fs);
      // final Set<String> classes = new HashSet<>();
      // classes.addAll(cpi.getClasses());
      // azzert.assertContains(classes, Object.class.getName());
      // azzert.assertContains(classes, String.class.getName());
      // azzert.assertContains(classes, Class.class.getName());
      // azzert.assertContains(classes, ClassRepositoryTest.class.getName());
      // azzert.assertContains(classes, Assert.class.getName());
      // azzert.assertContains(classes, Test.class.getName());
      // azzert.assertContains(classes, MyClass.class.getName());
      // } catch ( final Throwable ¢) {
      // ¢.printStackTrace();
      // fail(¢.getMessage());
      // }
    }

    @Test public void getClassesObject() {
      assert new ClassRepository.JRE().getClasses().contains("java.lang.Object");
    }
  }

  private static class NameDotSuffix {
    public final String name;
    public final String suffix;

    public NameDotSuffix(final File f) {
      this(f.getName());
    }

    public NameDotSuffix(final String s) {
      final int dot = s.lastIndexOf('.');
      name = dot < 0 ? s : s.substring(0, dot);
      suffix = dot < 0 ? "" : s.substring(dot);
    }

    public NameDotSuffix(final ZipEntry ze) {
      this(ze.getName().replace('/', '.'));
    }

    public boolean suffixIs(final String ¢) {
      return suffix.equalsIgnoreCase(¢);
    }
  }
}
