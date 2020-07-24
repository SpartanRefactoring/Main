package il.org.spartan.classfiles;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import il.org.spartan.streotypes.Utility;
import il.org.spartan.utils.Separate;

/** A utility class, serving as a façade to {@link CLASSPATH}, {@link JRE},
 * {@link EXTENSIONPATH}, {@link ZipFile} and {@link File} providing a unified
 * repository of all locations where Java binaries may be found.
 * @author Yossi Gil */
@Utility
public enum CLASSFILES {
  ;
  static final Set<ZipFile> zipsInUse = new HashSet<>();

  /** Where are all Java class files found
   * @return the list of directories and ZIP archives in the current search
   *         path. */
  public static Iterable<File> asFiles() {
    final ArrayList<File> $ = new ArrayList<>(JRE.asList());
    add($, EXTENSIONPATH.asArray(), CLASSPATH.asArray());
    return $;
  }
  /** Given the full name of a class, return a textual representation of the
   * location where the appropriate <tt>.class</tt> can be found.
   * @param className the full class name, where the inner- and anonymous- class
   *        separator is the <tt>$</tt> character, i.e., in the format returned
   *        by method {@link java.lang.Class#getName()}
   * @return a textual representation of the location in which the corresponding
   *         <tt>.class</tt> can be found, or <code><b>null</b></code> if this
   *         class has no corresponding <tt>.class</tt> file (e.g., in the case
   *         it is a primitive or an array type), or in the case that the
   *         corresponding <tt>.class</tt> file could not be found. */
  public static String location(final String className) {
    assert className != null;
    for (final File where : asFiles()) {
      final String $ = location(where, className);
      if ($ != null)
        return $;
    }
    return null;
  }
  /** Exercise this class by printing the result of its principal function.
   * @param __ unused */
  public static void main(final String[] __) {
    System.out.println(Separate.by(asFiles(), "\n"));
  }
  /** Given a {@link Class} object, return an open input stream to the
   * <tt>.class</tt> file where this class was implemented. (The input stream is
   * found by searching the class files repositories, and hence is not
   * guaranteed to be precisely that of the given object);
   * @param ¢ an arbitrary {@link Class} object
   * @return an {@link InputStream} to the result of best effort search for the
   *         <tt>.class</tt> where this class was implemented. Or,
   *         <code><b>null</b></code> if this class has no corresponding
   *         <tt>.class</tt> file (e.g., in the case it is a primitive or an
   *         array type), or in the case that the corresponding <tt>.class</tt>
   *         file could not be found. */
  public static InputStream open(final Class<?> ¢) {
    assert ¢ != null;
    return open(¢.getName());
  }
  /** Given the full name of a class, return an open input stream to the class
   * file where this class was implemented. (The input stream is found by
   * searching the class files repositories, and hence is not guaranteed to be
   * precisely that of the given object.)
   * @param fullClassName the full class name, where the inner- and anonymous-
   *        class separator is the <tt>$</tt> character, i.e., in the format
   *        returned by method {@link java.lang.Class#getName()}
   * @return an {@link InputStream} to the result of the best effort search for
   *         the <tt>.class</tt> where this class was implemented.
   *         <code><b>null</b></code> if this class has no corresponding
   *         <tt>.class</tt> file (e.g., in the case it is a primitive or an
   *         array type), or in the case that the corresponding <tt>.class</tt>
   *         file could not be found. */
  public static InputStream open(final String fullClassName) {
    assert fullClassName != null;
    for (final File f : asFiles()) {
      final InputStream $ = open(f, fullClassName);
      if ($ != null)
        try {
          $.available();
        } catch (final IOException ¢) {
          ¢.printStackTrace();
        }
      if ($ != null)
        return $;
    }
    return null;
  }
  public static void reset() {
    for (final ZipFile z : zipsInUse)
      try {
        z.close();
      } catch (final IOException __) {
        // Absorb (we do not care about errors)
        __.printStackTrace();
      }
    zipsInUse.clear();
  }
  private static void add(final ArrayList<File> ds, final String[]... directoryNamesArray) {
    for (final String[] directories : directoryNamesArray)
      add(ds, directories);
  }
  private static void add(final ArrayList<File> ds, final String[] directoryNames) {
    for (final String directory : directoryNames)
      ds.add(new File(directory));
  }
  private static String canonicalFileName(final String className) {
    return className.replace('.', File.separatorChar) + ".class";
  }
  private static String class2ZipFileName(final String className) {
    return className.replace('.', '/') + ".class";
  }
  private static String location(final File where, final String className) {
    return where.isDirectory() ? searchDirectory(where, className) == null ? null : where.getName()
        : searchZip(where, class2ZipFileName(className)) == null ? null : where.getName();
  }
  private static InputStream open(final File where, final String className) {
    return where.isDirectory() ? searchDirectory(where, className) : searchZip(where, class2ZipFileName(className));
  }
  private static InputStream searchDirectory(final File where, final String className) {
    final File $ = new File(where, canonicalFileName(className));
    try {
      return !$.exists() ? null : new FileInputStream($);
    } catch (final FileNotFoundException __) {
      return null;
    }
  }
  private static InputStream searchZip(final File where, final String fileName) {
    try (ZipFile $ = new ZipFile(where.getAbsoluteFile())) {
      final ZipEntry e = $.getEntry(fileName);
      if (e == null)
        return null;
      zipsInUse.add($);
      return $.getInputStream(e);
      /* for (final ZipEntry e : IterableAdapter.make(z.entries())) if
       * (e.getName().equals(fileName)) { zipsInUse.add(z); return
       * z.getInputStream(e); } z.close(); */
    } catch (final IOException __) {
      // Absorb (we do not care about errors)
      return null;
    }
  }
}
