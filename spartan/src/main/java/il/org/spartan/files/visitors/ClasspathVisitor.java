// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.files.visitors;

import il.org.spartan.classfiles.CLASSPATH;

/** A class realizing a file system traversal, similar to
 * {@link FileSystemVisitor}, except that the traversal is restricted to the
 * Java <em>classpath</em>.
 * <p>
 * The traversal is carried out by calling the class constructor
 * {@link ClasspathVisitor#ClasspathVisitor(il.org.spartan.files.visitors.FileSystemVisitor.EmptyAction, String...)}
 * to set the traversal actions, and then the inherited function {@link #go()}
 * to conduct the actual traversal.
 * <p>
 * @author Yossi Gil
 * @since 11/07/2007
 * @see il.org.spartan.files.visitors.FileSystemVisitor.EmptyAction */
public class ClasspathVisitor extends FileSystemVisitor {
  /** Create a new visitor object to scan the class path.
   * @param action a non-<code><b>null</b></code> specifying what to do in each
   *        visited file
   * @param extensions an array of non-<code><b>null</b></code> {@link String} s
   *        specifying which file extensions the visitor should call, e.g.,
   *        ".class", ".csv", etc. If this parameter is
   *        <code><b>null</b></code>, or of length 0, or contains a
   *        {@link String} of length 0, then the visitor is invoked for all
   *        files found in the supplied path.
   * @see #go() */
  public ClasspathVisitor(final EmptyAction action, final String... extensions) {
    super(CLASSPATH.asArray(), action, extensions);
  }
}
