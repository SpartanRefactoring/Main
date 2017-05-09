// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.files.visitors;

import java.io.*;

/** A class realizing a traversal of the file system, where the traversal is
 * restricted to <code>.class</code> files only
 * <p>
 * The traversal is carried out by calling one of the class constructors, e.g.,
 * {@link #ClassFilesVisitor(Collection, il.org.spartan.files.visitors.FileSystemVisitor.FileOnlyAction)}
 * or
 * {@link #ClassFilesVisitor(String, il.org.spartan.files.visitors.FileSystemVisitor.FileOnlyAction)}
 * to set the visitation range, and the
 * {@link il.org.spartan.files.visitors.FileSystemVisitor.Action} to be carried
 * for each visited file.
 * @see ClassFilesClasspathVisitor
 * @author Yossi Gil
 * @since 11/07/2007 */
public class ClassFilesVisitor extends FileSystemVisitor {
  private static final String[] CLASS_FILE_EXTENSIONS = new String[] { ".class" };

  public ClassFilesVisitor(final File[] from, final FileOnlyAction visitor) {
    super(from, visitor, CLASS_FILE_EXTENSIONS);
  }
  public ClassFilesVisitor(final Iterable<String> from, final FileOnlyAction visitor) {
    super(from, visitor, CLASS_FILE_EXTENSIONS);
  }
  public ClassFilesVisitor(final String from, final FileOnlyAction visitor) {
    super(from, visitor, CLASS_FILE_EXTENSIONS);
  }
  public ClassFilesVisitor(final String[] from, final FileOnlyAction visitor) {
    super(from, visitor, CLASS_FILE_EXTENSIONS);
  }
}
