package il.org.spartan.classfiles;

import static fluent.ly.___.*;

import java.io.*;

import org.jetbrains.annotations.*;

/** A collection of functions representing the translation of class names to
 * path and vice versa.
 * @author Yossi Gil
 * @since 12/07/2007 */
public enum Filename {
  ;
  /** The '.' character, used in separation of fully qualified class names. */
  public static final char DOT = '.';

  /** Obtain the relative path name of a class
   * @param ¢ an arbitrary class
   * @return the relative path name, with respect to JAVA packages directory
   *         system, of the parameter */
  public static String class2path(@NotNull final Class<?> ¢) {
    return class2path(¢.getCanonicalName());
  }
  /** obtain the relative path name of the class of a given object
   * @param ¢ an arbitrary object
   * @return relative path of this class */
  public static Object class2path(@NotNull final Object ¢) {
    return class2path(¢.getClass());
  }
  /** Obtain the relative path name of a class
   * @param className the fully qualified name of a class
   * @return the relative path name, with respect to JAVA packages directory
   *         system, of the parameter */
  public static String class2path(@NotNull final String className) {
    return className.replace(DOT, File.separatorChar);
  }
  public static String filePart(@NotNull final String fileName) {
    nonnull(fileName);
    System.out.println("Replacing " + fileName);
    final String $ = fileName.replaceAll("/", "\\");
    System.out.println("normalizedName = " + $);
    return $.substring($.lastIndexOf('\\') + 1);
  }
  public static String getTrailer(@NotNull final String ¢) {
    return ¢.substring(¢.lastIndexOf('.') + 1);
  }
  /** Obtain the leading portion of a fully qualified class name.
   * @param name a class name to process
   * @return the longest prefix of name that is followed by a {@link #DOT}, or
   *         the empty {@link String} if no such prefix exists.
   * @see #tailPart(String) */
  @NotNull public static String headPart(@NotNull final String name) {
    final int $ = name.lastIndexOf(DOT);
    return $ < 0 ? "" : name.substring(0, $);
  }
  public static boolean isAllInner(@NotNull final String name) {
    return isInner(name) && !tailPart(name).matches(".*[$][0-9].*");
  }
  /** determine whether a class is anonymous
   * @param name a class name to process
   * @return <code><b>true</b></code> <em>iff</em>the class is an anonymous one,
   *         i.e., defined in the context of a <code><b>new</b></code>
   *         expression. */
  public static boolean isAnonymous(@NotNull final String name) {
    return trailerPart(name).matches("[0-9]+");
  }
  /** determine whether a class is inner
   * @param name a class name to process
   * @return <code><b>true</b></code> <em>iff</em>the class is an inner class,
   *         i.e., defined within another class. */
  public static boolean isInner(@NotNull final String name) {
    return trailerPart(name).matches("[A-Za-z_¢$].*");
  }
  /** determine whether a class is local
   * @param name a class name to process
   * @return <code><b>true</b></code> <em>iff</em>the class is a local one,
   *         i.e., defined within a function. */
  public static boolean isLocal(@NotNull final String name) {
    return trailerPart(name).matches("[0-9][A-Za-z_¢$].*");
  }
  public static String name2Canonical(final String name) {
    for (String before = name, $;; before = $) {
      $ = before.replaceFirst("\\$([a-zA-Z_¢$][a-zA-Z0-9_¢$]*)$", ".$1");
      if ($.equals(before))
        return $;
    }
  }
  /** Convert a relative file name into a class name.
   * @param path a relative path name, with respect to JAVA packages directory
   *        system
   * @return the fully qualified name of the class residing in this file. */
  public static String path2class(@NotNull final String path) {
    return path.replaceAll("\\.class$", "").replace('/', DOT).replace('\\', DOT);
  }
  /** Convert a file name as found in the file system, to a class name.
   * @param path a file name to be converted
   * @param root the root of the packages directory
   * @return the fully qualified name of the class residing in this file. */
  @NotNull public static String path2class(@NotNull final String path, final String root) {
    return path2class(removeRoot(path, root));
  }
  /** Convert a relative directory name into a package name.
   * @param path a relative path name, with respect to JAVA packages directory
   *        system
   * @return the fully qualified name of the class residing in this file. */
  public static String path2package(@NotNull final String path) {
    return path.replaceAll("[/]", ".");
  }
  /** Convert an absolute directory name as found in the file system, to a class
   * name.
   * @param path a file name to be converted
   * @param root the root of the packages directory
   * @return the fully qualified name of the class residing in this file. */
  public static String path2package(@NotNull final String path, final String root) {
    return path2package(removeRoot(path, root));
  }
  /** Trim the root portion of an absolute path
   * @param path the absolute path to be trimmed
   * @param root the root portion to be trimmed from the path
   * @return path, but without the root portion */
  public static String removeRoot(@NotNull final String path, final String root) {
    return path.replace(root + File.separator, "");
  }
  /** Obtain the last portion of a fully qualified class name.
   * @param name a class name to process
   * @return that portion of the name that follows the last occurrence of the
   *         {@link #DOT}, or the entire name, if name does not contain this
   *         character.
   * @see #tailPart(String) */
  @NotNull public static String tailPart(@NotNull final String name) {
    final int $ = name.lastIndexOf(DOT);
    return $ < 0 ? name : name.substring($ + 1);
  }
  @NotNull public static String trailerPart(@NotNull final String name) {
    @NotNull final String $ = tailPart(name);
    final int index = $.lastIndexOf('$');
    return index < 1 ? "" : $.substring(index + 1);
  }
}
