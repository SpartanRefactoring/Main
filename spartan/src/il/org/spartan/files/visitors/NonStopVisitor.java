// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.files.visitors;

import static il.org.spartan.utils.___.*;

import java.io.*;

import org.jetbrains.annotations.*;

import il.org.spartan.files.visitors.FileSystemVisitor.Action.*;

/** A class realizing the {@link FileSystemVisitor} functionality, except that
 * it does not allow throws of {@link StopTraversal} exceptions.
 * @author Yossi Gil
 * @since 13/07/2007 */
public class NonStopVisitor extends FileSystemVisitor {
  public NonStopVisitor(final File from, final NonStopAction action, final String... extensions) {
    super(from, action, extensions);
  }

  public NonStopVisitor(final File[] from, final NonStopAction action, final String... extensions) {
    super(from, action, extensions);
  }

  public NonStopVisitor(@NotNull final Iterable<String> from, final NonStopAction action, final String... extensions) {
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
    } catch (@NotNull final StopTraversal e) {
      unreachable();
    }
  }
}
