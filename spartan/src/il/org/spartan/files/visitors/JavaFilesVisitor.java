// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.files.visitors;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.annotation.*;

/** A class realizing a traversal of the file system, where the traversal is
 * restricted to <code>.java</code> files only
 * <p>
 * The traversal is carried out by calling one of the class constructors, to set
 * the visitation range, and the
 * {@link il.org.spartan.files.visitors.FileSystemVisitor.Action} to be carried
 * for each visited file.
 * <p>
 * @see JavaFilesVisitor
 * @author Yossi Gil
 * @since 11/07/2007 */
public class JavaFilesVisitor extends NonStopVisitor {
  private static final String[] JAVA_FILES_EXTENSION = new String[] { ".java" };

  public JavaFilesVisitor( final Collection<String> from, final FileOnlyAction action) {
    super(from, action, JAVA_FILES_EXTENSION);
  }
  public JavaFilesVisitor(final File from, final FileOnlyAction action) {
    super(from, action, JAVA_FILES_EXTENSION);
  }
  public JavaFilesVisitor(final File[] from, final FileOnlyAction action) {
    super(from, action, JAVA_FILES_EXTENSION);
  }
  public JavaFilesVisitor(final String from, final FileOnlyAction action) {
    super(from, action, JAVA_FILES_EXTENSION);
  }
  public JavaFilesVisitor(final String[] from, final FileOnlyAction action) {
    super(from, action, JAVA_FILES_EXTENSION);
  }
}
