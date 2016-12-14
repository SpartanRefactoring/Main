package il.org.spartan.classfiles;

import static il.org.spartan.utils.___.*;

import java.io.*;
import java.util.*;
import java.util.zip.*;

import org.jetbrains.annotations.*;

import il.org.spartan.streotypes.*;
import il.org.spartan.utils.*;

/** A utility class, serving as a façade to {@link CLASSPATH}, {@link JRE},
 * {@link EXTENSIONPATH}, {@link ZipFile} and {@link File} providing a unified
 * repository of all locations where Java binaries may be found.
 * @author Yossi Gil */
@Utility public enum CLASSFILES {
  ;
  @NotNull static final Set<ZipFile> zipsInUse = new HashSet<>();

  /** Where are all Java class files found
   * @return the list of directories and ZIP archives in the current search
   *         path. */
  @NotNull public static Iterable<File> asFiles() {
    @NotNull final ArrayList<File> $ = new ArrayList<>();
    $.addAll(JRE.asList());
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
  public static String location(@NotNull final String className) {
    nonnull(className);
    for (@NotNull final File where : asFiles()) {
      @Nullable final String $ = location(where, className);
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
  @Nullable public static InputStream open(@NotNull final Class<?> ¢) {
    nonnull(¢);
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
  public static InputStream open(@NotNull final String fullClassName) {
    nonnull(fullClassName);
    for (@NotNull final File f : asFiles()) {
      @Nullable final InputStream $ = open(f, fullClassName);
      if ($ != null)
        try {
          $.available();
        } catch (@NotNull final IOException ¢) {
          ¢.printStackTrace();
        }
      if ($ != null)
        return $;
    }
    return null;
  }

  public static void reset() {
    for (@NotNull final ZipFile z : zipsInUse)
      try {
        z.close();
      } catch (@NotNull final IOException __) {
        // Absorb (we do not care about errors)
        __.printStackTrace();
      }
    zipsInUse.clear();
  }

  private static void add(@NotNull final ArrayList<File> ds, @NotNull final String[]... directoryNamesArray) {
    for (@NotNull final String[] directories : directoryNamesArray)
      add(ds, directories);
  }

  private static void add(@NotNull final ArrayList<File> ds, @NotNull final String[] directoryNames) {
    for (@NotNull final String directory : directoryNames)
      ds.add(new File(directory));
  }

  @NotNull private static String canonicalFileName(@NotNull final String className) {
    return className.replace('.', File.separatorChar) + ".class";
  }

  @NotNull private static String class2ZipFileName(@NotNull final String className) {
    return className.replace('.', '/') + ".class";
  }

  private static String location(@NotNull final File where, @NotNull final String className) {
    return where.isDirectory() ? searchDirectory(where, className) == null ? null : where.getName()
        : searchZip(where, class2ZipFileName(className)) == null ? null : where.getName();
  }

  @Nullable private static InputStream open(@NotNull final File where, @NotNull final String className) {
    return where.isDirectory() ? searchDirectory(where, className) : searchZip(where, class2ZipFileName(className));
  }

  private static InputStream searchDirectory(final File where, @NotNull final String className) {
    @NotNull final File $ = new File(where, canonicalFileName(className));
    try {
      return !$.exists() ? null : new FileInputStream($);
    } catch (@NotNull final FileNotFoundException __) {
      return null;
    }
  }

  private static InputStream searchZip(@NotNull final File where, @NotNull final String fileName) {
    try {
      @NotNull final ZipFile $ = new ZipFile(where.getAbsoluteFile());
      final ZipEntry e = $.getEntry(fileName);
      if (e == null) {
        $.close();
        return null;
      }
      zipsInUse.add($);
      return $.getInputStream(e);
      /* for (final ZipEntry e : IterableAdapter.make(z.entries())) if
       * (e.getName().equals(fileName)) { zipsInUse.add(z); return
       * z.getInputStream(e); } z.close(); */
    } catch (@NotNull final IOException __) {
      // Absorb (we do not care about errors)
    }
    return null;
  }
}
