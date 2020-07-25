// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.files.visitors;

import static fluent.ly.___.unreachable;

import java.io.File;
import java.io.IOException;

import il.org.spartan.files.visitors.FileSystemVisitor.Action.StopTraversal;

/** A class realizing the {@link FileSystemVisitor} functionality, except that
 * it does not allow throws of {@link StopTraversal} exceptions.
 *
 * @author Yossi Gil
 * @since 13/07/2007 */
public class NonStopVisitor extends FileSystemVisitor {
  public NonStopVisitor(final File from, final NonStopAction action, final String... extensions) {
    super(from, action, extensions);
  }

  public NonStopVisitor(final File[] from, final NonStopAction action, final String... extensions) {
    super(from, action, extensions);
  }

  public NonStopVisitor(final Iterable<String> from, final NonStopAction action, final String... extensions) {
    super(from, action, extensions);
  }

  public NonStopVisitor(final String from, final NonStopAction action, final String[] extensions) {
    super(from, action, extensions);
  }

  public NonStopVisitor(final String[] from, final NonStopAction action, final String... extensions) {
    super(from, action, extensions);
  }

  @Override public final void go() throws IOException {
    try {
      super.go();
    } catch (final StopTraversal e) {
      unreachable();
    }
  }
}
