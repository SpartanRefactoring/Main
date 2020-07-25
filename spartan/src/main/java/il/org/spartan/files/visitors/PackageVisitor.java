// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.files.visitors;

/** A class realizing a traversal of the class path, similar to
 * {@link ClasspathVisitor}, except that the traversal is restricted to the
 * directories only.
 * <p>
 * The traversal is carried out by calling the class constructor to set the
 * traversal actions, and then the inherited function {@link #go()} to conduct
 * the actual traversal.
 * <p>
 *
 * @author Yossi Gil
 * @since 11/07/2007 */
public class PackageVisitor extends ClasspathVisitor {
  /** Create a new visitor object to scan the class path.
   *
   * @param action a non-<code><b>null</b></code> specifying what to do in each
   *               visited directory.
   * @see #go() */
  public PackageVisitor(final DirectoryOnlyAction action) {
    super(action);
  }
}
